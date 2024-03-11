package src;

import static src.Util.distance;

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {
  private Map<City,HashSet<Road>> cityRoads;
  private Map<String,City> cityName;
  private HashMap<Integer, City> citiesId;
  private Deque<City> fileBfs;
  private Set<City> isVisitBfs;

  public Graph(File cities, File roads) {
    cityRoads = new HashMap<City,HashSet<Road>>();

    citiesId = new HashMap<Integer,City>();
    cityName = new HashMap<String,City>();

    fileBfs = new ArrayDeque<City>();
    isVisitBfs = new HashSet<City>();


    try(FileReader fileReader = new FileReader(cities);

        BufferedReader bufferedReader =  new BufferedReader(fileReader)){
      String line;

      while((line = bufferedReader.readLine()) != null){

        String [] data = line.split(",");

        int id = Integer.parseInt(data[0]);
        String name = data[1];
        double longitude = Double.parseDouble(data[2]);
        double latitude = Double.parseDouble(data[3]);
        City city = new City(id,name,longitude,latitude);
        citiesId.put(id,city);
        cityName.put(name,city);
      }
    }catch(IOException e){
      e.printStackTrace();
    }

    try(FileReader fileReader = new FileReader(roads);
        BufferedReader bufferedReader =  new BufferedReader(fileReader)){
      String line;

      while((line = bufferedReader.readLine()) != null){

        String [] data = line.split(",");

        int cityStartId = Integer.parseInt(data[0]);
        int cityDestId = Integer.parseInt(data[1]);
        City cityStart = citiesId.get(cityStartId);
        City cityDest = citiesId.get(cityDestId);

        Road road = new Road(cityStart, cityDest);

        if(cityRoads.containsKey(cityStart)){
          cityRoads.get(cityStart).add(road);

        }else{
          HashSet <Road> hashSetRoad = new HashSet();
          hashSetRoad.add(road);
          cityRoads.put(cityStart,hashSetRoad);
        }
      }
    }catch(IOException e){
      e.printStackTrace();
    }
  }

  /**
   * Méthode qui calcule l'itinéraire entre deux villes avec le moins de routes possible
   * Utiliser la méthode de BFS
   * @param depart le départ de l'itinéraire
   * @param destination la destination de l'itinéraire
   */
  public void calculerItineraireMinimisantNombreRoutes(String depart, String destination) {
    Map<City,City> predecessor = new HashMap<City,City>();
    ArrayList <String> chemin = new ArrayList<String>();
    City start = cityName.get(depart);
    City goingTo;
    City visit;
    Boolean find = false;


    fileBfs.add(start);
    isVisitBfs.add(start);



    while(!fileBfs.isEmpty() && !find){
      visit = fileBfs.removeFirst();

      if(cityRoads.get(visit) == null){
        continue;
      }else{
        for (Road road : cityRoads.get(visit)) {
          goingTo = road.getCityDest();
          if(!isVisitBfs.contains(goingTo)){
            fileBfs.add(goingTo);
            isVisitBfs.add(goingTo);
            predecessor.put(goingTo,visit);

          }
          if(destination.equals(goingTo)){
            find = true;
          }
        }

      }
    }

    chemin.add(cityName.get(destination).getName());
    City actuel = cityName.get(destination);

    while(actuel != null){
      chemin.add(actuel.getName());
      actuel = predecessor.get(actuel);

    }

    Collections.reverse(chemin);
    double totalDistance = 0;

    for (int i = 0; i < chemin.size() - 1; i++) {
      City currentCity = cityName.get(chemin.get(i));
      City nextCity = cityName.get(chemin.get(i + 1));
      double distance = distance(currentCity.getLatitude(),currentCity.getLongitude(), nextCity.getLatitude(),nextCity.getLongitude());
      totalDistance= totalDistance + distance;

      System.out.println(currentCity.getName() + " -> " + nextCity.getName() + " (" + distance + " km)");

    }

    System.out.println("Trajet de " + chemin.get(0) + " a " + chemin.get(chemin.size() -1) + " : " + chemin.size() + " routes et " + totalDistance + " kms");


  }

  /**
   * Méthode qui calcule l'itinéraire entre deux villes avec le moins de kilomètres
   * Utiliser la méthode de Dijkstra
   * @param depart le départ de l'itinéraire
   * @param destination la destination de l'itinéraire
   */
  public void calculerItineraireMinimisantKm(String depart, String destination) {
  }
}
