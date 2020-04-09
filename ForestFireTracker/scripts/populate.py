"""
populate.py - generates believeable data and populates the database
"""
import random
import datetime
import time
import math

POP_DENSITY_CONSTANT = (math.pi/180)*(6731**2) # in squared kilometers

def str_time_prop(start, end, fmt, prop):
    """Get a time at a proportion of a range of two formatted times.

    start and end should be strings specifying times formated in the
    given format (strftime-style), giving an interval [start, end].
    prop specifies how a proportion of the interval to be taken after
    start.  The returned time will be in the specified format.
    """

    stime = time.mktime(time.strptime(start, fmt))
    etime = time.mktime(time.strptime(end, fmt))

    ptime = stime + prop * (etime - stime)

    return time.strftime(fmt, time.localtime(ptime))


def random_date(start, end, prop):
    """
    get random date between start and end
    """
    return str_time_prop(start, end, '%Y-%m-%d %I:%M:%S', prop)

def get_rand_date():
    """
    actually get the random date lol
    """
    return random_date("1-1-2000 0:00:00", "1-1-2020 0:00:00", random.random())

def calculate_pop_density():


def populate_forests(regions):
    """
    Generate a list of forests
    Needs regions
    using xml files?
    
    :param regions list:
        List of regions
    """

def populate_fires(forests):
    """
    look at an actual database for those?
    :param forests list:
        list of forests, need a forest in order to have a forest fire
    """

def populate_reports(fires):
    """
    Generate a bunch of dummy reports
    :param fires list:
        the list of forest fire entries
    """
    reports = []
    for i in range(0, 300):
        createDate = get_rand_date()
        publishDate = datetime.date.today()
        title = "Report_fire_" + str(createDate)
        startDate = datetime.date.today() # TODO: need forest fires for that
        endDate = datetime.date.today() # TODO: need forest fires for that
        reports.append((createDate, publishDate, title, startDate, endDate))
    return reports

def populate_authors(reports):
    """
    Generates random authors and pushes them to the DB
    :param reports list:
        list of report entries to title and createDate
    """
    authors = []
    author_ctr = 0
    for i in range(0, 300):
        email = 'author.mcauthorface' + str(author_ctr) + '@gmail.com'
        author_ctr += 1
        name = 'Author McAuthorFace'
        report_tuple = random.choice(reports)
        title = report_tuple[2]
        createDate = report_tuple[0]
        authors.append((email, name, title, createDate))
    return authors

def populate_animals():
    """"
    """"
    # TODO

def populate_populations():
    populations = []
    for i in range(0, 9):


    # TODO

def populate_population_samples():
    """
    """
    # TODO

def populate_regions():
    """
    """
    # TODO


def main():
    """
    main function of the script
    """
    # generate lists of tuples
    regions = []
    forests = []
    fires = [] # TODO
    reports = populate_reports(fires)
    authors = populate_authors(reports)
    # TODO
    
    # convert these tuples to json and/or insert into tables
    # could also just generate a .sql file
    # TODO

if __name__ == '__main__':
    main()
