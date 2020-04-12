SELECT p.species,p.fname,p.headcount,p.endangered,a.totalheadcount,a.behavior          
FROM populations p, animals a                                                                           
WHERE p.species=a.species