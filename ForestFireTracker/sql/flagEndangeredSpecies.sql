CREATE OR REPLACE FUNCTION flagEndangeredSpecies() returns setof varchar(30) AS $$
DECLARE
r record;
BEGIN
FOR r IN SELECT DISTINCT t.species
FROM (
SELECT DISTINCT t1.species
FROM populations p, (
SELECT DISTINCT p.species, SUM((
SELECT DISTINCT ps.sampleheadcount
FROM populationsamples ps
WHERE ps.samplingtime IN (
SELECT MAX(ps.samplingtime)
FROM populationsamples ps
GROUP BY ps.fname)) - (
SELECT DISTINCT ps.sampleheadcount
FROM populationsamples ps
WHERE ps.samplingtime IN (
SELECT MIN(ps.samplingtime)
FROM populationsamples ps
WHERE ps.samplingtime > (clock_timestamp() - interval '10 years')
GROUP BY ps.fname))) AS popChange
FROM populations p, populationsamples ps
WHERE p.species = ps.species
AND p.fname = ps.fname
GROUP BY p.species
) t1, (
SELECT DISTINCT p.species, a.totalheadcount, SUM(f.fsurfacearea) AS popSurfaceArea
FROM populations p, forests f, animals a
WHERE p.fname = f.fname
AND p.species = a.species
GROUP BY p.species, a.totalheadcount
) t2
WHERE (((t1.popChange / t2.totalheadcount) >= 0.5) AND ((t1.popChange / t2.totalheadcount) <= 0.7))
OR t2.popSurfaceArea < 5000
) t
LOOP
UPDATE populations
SET endangered = true
WHERE r IS NOT NULL AND populations.species = r.species;
END LOOP;
END $$
LANGUAGE 'plpgsql';