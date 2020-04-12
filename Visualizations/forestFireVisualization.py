import pandas as pd
import matplotlib.pyplot as plt
import geopandas as gpd
from shapely.geometry import Point

df = pd.read_csv("~/Desktop/forestfiresinfo.csv")

def format_dates(time_series):
	new_series = [time.split(' ')[0] for time in time_series]
	new_series = pd.to_datetime(new_series)
	return new_series

def create_visual(size, info_df):
	fig,ax = plt.subplots(figsize=size)
	world = gpd.read_file(gpd.datasets.get_path('naturalearth_lowres'))
	world.plot(ax=ax, color='gray')

	geom = [Point(coord) for coord in zip(info_df["center_lon"], info_df["center_lat"])]
	plot_df = gpd.GeoDataFrame(info_df, geometry=geom)

	plot_df[(plot_df["difference"] > 0)].plot(ax=ax, markersize=450, color='green', label='short')
	plot_df[plot_df["difference"] >= 100].plot(ax=ax, markersize=900, color='blue', label='medium')
	plot_df[plot_df["difference"] >= 300].plot(ax=ax, markersize=1350, color='purple', label='long')
	plot_df[plot_df["difference"] >= 500].plot(ax=ax, markersize=1800, color='red', label='very long')
	plt.legend(prop={'size':25}, title='Duration', loc=3)
	plt.title("Forest Fires")

	plt.show()

def main():

	# Clean up data and add necessary columns
	df["Starttime"] = format_dates(df["Starttime"])
	df["endtime"] = format_dates(df["endtime"])
	df["difference"] = (df["endtime"] - df["Starttime"]).dt.days
	df["center_lat"] = (df["latmin"] + df["latmax"]) / 2.0
	df["center_lon"] = (df["lonmin"] + df["lonmax"]) / 2.0

	# Create dataframe of needed columns
	info_df = df[["fname", "difference", "center_lat", "center_lon"]]

	create_visual((25,25), info_df)
	

if __name__ == "__main__":
	main()


