import os
import pandas as pd

shared_data_path= "../../../experimentations/shared_data/"

# script sorting all csv in this folder and its subfolders

def sortCsv(file_path, file_name):
    csv=pd.read_csv(file_path)
    name= file_name.split("_")
    csv.sort_values([name[1], name[0]],axis=0, ascending=True, inplace=True)
    print(name[0])
    csv.to_csv(file_path, sep=',', index=False)

for dirNames in os.scandir(shared_data_path):
    if(os.path.isdir(dirNames)):
        for filenames in os.scandir(dirNames):
            if(os.path.isfile(filenames)):
                print(filenames.name)
                sortCsv(filenames.path, filenames.name)
