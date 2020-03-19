CREATE OR REPLACE FUNCTION flagEndangeredSpecies() returns setof varchar(30) AS $$
DECLARE
r record;
BEGIN
FOR r IN SELECT species AS endangeredSpecies
FROM
((
SELECT p.species, SUM((
SELECT DISTINCT ps.sampleheadcount AS curSampleHeadcount
FROM populationsamples ps
WHERE ps.samplingtime IN (
SELECT MAX(ps.samplingtime)
FROM populationsamples ps
GROUP BY ps.species)) - (
SELECT DISTINCT ps.sampleheadcount AS temp
FROM populationsamples ps
WHERE ps.samplingtime IN (
SELECT MIN(ps.samplingtime)
FROM populationsamples ps
WHERE ps.samplingtime > (clock_timestamp() - interval '10 years')
GROUP BY ps.species))) AS popChange
FROM populations p, populationsamples ps, animals a
WHERE p.species = ps.species
AND ps.species = a.species
GROUP BY p.species
)
INTERSECT
(
SELECT DISTINCT p.species, SUM(f.fsurfacearea) AS popSurfaceArea
FROM populations p, populationsamples ps, forests f
WHERE p.species = ps.species
AND ps.fname = p.fname
AND f.fname = f.fname
GROUP BY p.species
))t loop

IF (((t.popChange/a.totalheadcount) >= 0.5) AND ((t.popChange/a.totalheadcount) <= 0.7)) THEN
RETURN;
ELSIF (t.popSurfaceArea < 5000) THEN
RETURN;
ELSE
RETURN NEXT r;
END IF;
END loop;
END $$
LANGUAGE 'plpgsql';