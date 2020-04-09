Alter Table forestfires Drop Constraint fdateConstraint;
Alter Table reports Drop Constraint rdateConstraint;
Alter Table geogranges Drop Constraint geogConstraint;

Alter Table forestfires Add Constraint fdateConstraint CHECK
(
    startTime < endTime
);

Alter Table reports Add Constraint rdateConstraint CHECK
(
    createDate < publishDate
);

Alter Table geogranges Add Constraint geogConstraint CHECK
(
    latMin < latMax And
    lonMin < lonMax
);

