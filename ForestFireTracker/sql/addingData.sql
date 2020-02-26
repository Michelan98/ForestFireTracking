delete from countries;
delete from animals;
delete from regions;
delete from forests;
delete from geogranges;


/* 0-NA, 1-SA, 2-Europe, 3-Asia, 4-Africa, 5-Austrailia, 6-Antarctica */
insert into countries values ('Sweden', 2),
                             ('Canada', 0),
                             ('USA', 0),
                             ('Italy', 2),
                             ('Brazil', 1),
                             ('Spain', 2),
                             ('Austrailia', 5),
                             ('New Zealand', 5),
                             ('Angola', 4),
                             ('Democratic Rep. of Congo', 4),
                             ('Indonesia', 3),
                             ('Russia', 3);

/* Bird-0, Mammal-1, Amphibian-2, Reptile-3, Insect-4*/
/* Mostly nocturnal-0, Mostly diurnal-1*/
insert into animals values ('Canis lupus', 'North American gray wolf', 1, 15400, 0),
                           ('Vulpes vulpes', 'Red fox', 1, 1120000, 0),
                           ('Sciurus vulgaris', 'Eurasian red squirrel', 1, 490000, 1),
                           ('Dendroctonus ponderosae', 'Mountain pine beetle', 4, 570000000,1),
                           ('Rhinella marina', 'Cane toad', 2, 198000000, 0),
                           ('Python bivittatus', 'Burmese python', 3, 56000, 0),
                           ('Myuchelys purvisi', 'Sawshelled turtle', 3, 750, 1),
                           ('Ramphastos toco', 'Toco toucan', 0, 12000, 1),
                           ('Dasyornis brachypterus', 'Eastern bristlebird', 0, 2500, 1);

/* tropical-0, temperate-1, arid-2*/
/* flat-0, hilly-1, mountainous-2 */
insert into regions values ('Eastern New South Wales', 2, 2, 'Austrailia'),
                           ('British Columbia', 1, 1, 'Canada'),
                           ('Southern California', 1, 2, 'USA'),
                           ('Rondonia', 0, 0, 'Brazil'),
                           ('Moxico', 0, 0, 'Angola'),
                           ('Colorado', 1, 2, 'USA'),
                           ('Siberia', 2, 0, 'Russia');
/* Area in sq.km. */
insert into forests values ('Amazon rain forest', 5500000, 'rain forest', 'Rondonia'),
                           ('Congo basin rain forest', 2000000, 'rain forest', 'Moxico'),
                           ('Pike national forest', 4478, 'temperate', 'Colorado'),
                           ('Cleveland national forest', 1900, 'temperate', 'Southern California'),
                           ('Blue gum high forest', 1.36, 'temperate', 'Eastern New South Wales'),
                           ('Siberian forests (all)', 2500000, 'boreal', 'Siberia');

insert into geogranges values ('Siberia', 49.673, 77.877, 62.386, 158.682),
                              ('Rondonia', -13.690, -7.985, -66.789, -59.791),
                              ('Moxico', -16.160, -10.582, 17.920, 24.054),
                              ('Colorado', 36.994, 41.002, -109.000, -101.966),
                              ('Southern California', 32.742, 34.269, -120.633, -114.162);