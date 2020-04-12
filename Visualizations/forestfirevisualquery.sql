with temp as (
    select starttime, endtime, f.fname, rname
    from forestfires ff, forests f
    where ff.fname = f.fname
)
select starttime, endtime, fname, latmin, latmax, lonmin, lonmax
from temp t, geogranges g
where t.rname = g.rname
