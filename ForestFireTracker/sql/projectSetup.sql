Drop Table animals;--done
Drop Table forestFires;--done
Drop Table countries;--done
Drop Table regions;--done
Drop Table populationSamples;--done
Drop Table reports;
Drop Table authors;
Drop Table forests;--done
Drop Table populations;--done
Drop Table geogRanges;--done
Drop Table zones;-- ? not sure if we actually need that one

Create Table animals
(
    species varchar(30) not null,
    commonName varchar(30),
    type integer, -- Map animal type (mammal, bird, reptile, ...) to int (or enum): less data
    totalHeadcount integer,
    behavior integer, -- also mapped to int or enum
    primary key(species)
);

Create Table countries
(
    cname varchar(30) not null,
    continent integer, -- mapped to int for data optimization, pretty sure there's ways to having enum in postgre DB
    primary key(cname)
);

Create Table regions
(
    rname varchar(30) not null,
    climate integer, -- mapped to int for data optimization, pretty sure there's ways to having enum in postgre DB
    topography integer, -- again, or enum
    cname varchar(30) not null,
    primary key(rname),
    foreign key(cname) references countries(cname)
);

Create Table forests
(
    fname varchar(30) not null,
    fsurfaceArea float,
    type varchar(30),
    rname varchar(30) not null,
    primary key(fname),
    foreign key(rname) references regions(rname)
);

Create Table forestFires
(
    startTime timestamp not null,
    endTime timestamp not null,
    surfaceArea float, -- Is the zones table really needed?
    fname varchar(30) not null,
    primary key(startTime, endTime),
    foreign key(fname) references forests(fname)
);

Create Table populations
(
    species varchar(30) not null,
    fname varchar(30) not null,
    density float,
    headcount integer,
    endangered boolean,
    primary key(species, fname),
    foreign key(species) references animals(species),
    foreign key(fname) references forests(fname)
);

Create Table populationSamples
(
    sampleId uuid not null, -- universally unique id
    samplingTime timestamp,
    sampleHeadcount integer,
    species varchar(30) not null,
    fname varchar(30) not null,
    primary key(sampleId),
    foreign key(species) references animals(species), -- indirectly references populations by having the same foreign keys
    foreign key(fname) references forests(fname)
);

Create Table geogRanges
(
    rname varchar(30) not null,
    latMin float not null, -- don't know if floats are correct, apparently there's some module to deal with that
    latMax float not null,
    lonMin float not null,
    lonMax float not null,
    primary key(latMin, latMax, lonMin, lonMax),
    foreign key(rname) references regions(rname)
);
