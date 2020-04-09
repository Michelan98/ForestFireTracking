Drop View animals_in_forests_view;
Drop View forests_in_countries_view;

Create View animals_in_forests_view As
Select commonname, fname, headcount
From animals a, populations p
Where a.species=p.species;

Create View forests_in_countries_view As
Select f.fname, f.rname, c.cname, c.continent
From forests f
Inner Join regions r on r.rname=f.rname
Inner Join countries c on r.cname=c.cname
