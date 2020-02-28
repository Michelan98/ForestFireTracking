-- Idea:
-- For each forest fire, get the population samples right before and right after
-- the fire for each population in the forest that the fire affected

(
Select ps.*, ff.starttime, ff.endtime
From populationsamples ps
Inner Join forests f on f.fname=ps.fname
Inner Join forestfires ff on ff.fname=f.fname
Where (ps.samplingtime < ff.startTime)
Order By ps.samplingtime Desc
--Limit 1
)
Union
(
Select ps.*, ff.starttime, ff.endtime
From populationsamples ps
Inner Join forests f on f.fname=ps.fname
Inner Join forestfires ff on ff.fname=f.fname
Where (ps.samplingtime > ff.endtime)
Order By ps.samplingtime Asc 
--Limit 1
)
;
