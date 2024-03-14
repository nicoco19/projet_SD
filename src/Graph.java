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

  private Map<City, HashSet<Road>> cityRoads;
  private Map<String, City> cityName;
  private HashMap<Integer, City> citiesId;
  private Deque<City> fileBfs;
  private Set<City> isVisitBfs;

  public Graph(File cities, File roads) {
    cityRoads = new HashMap<City, HashSet<Road>>();

    citiesId = new HashMap<Integer, City>();
    cityName = new HashMap<String, City>();

    fileBfs = new ArrayDeque<City>();
    isVisitBfs = new HashSet<City>();

    try (FileReader fileReader = new FileReader(cities);
        BufferedReader bufferedReader = new BufferedReader(fileReader)) {
      String line;

      while ((line = bufferedReader.readLine()) != null) {
        String[] data = line.split(",");

        int id = Integer.parseInt(data[0]);
        String name = data[1];
        double longitude = Double.parseDouble(data[2]);
        double latitude = Double.parseDouble(data[3]);
        City city = new City(id, name, longitude, latitude);
        citiesId.put(id, city);
        cityName.put(name, city);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    try (FileReader fileReader = new FileReader(roads);
        BufferedReader bufferedReader = new BufferedReader(fileReader)) {
      String line;

      while ((line = bufferedReader.readLine()) != null) {

        String[] data = line.split(",");

        int cityStartId = Integer.parseInt(data[0]);
        int cityDestId = Integer.parseInt(data[1]);
        City cityStart = citiesId.get(cityStartId);
        City cityDest = citiesId.get(cityDestId);

        Road road = new Road(cityStart, cityDest);

        // verifie l'existence de cityStart
        if (!cityRoads.containsKey(cityStart)) {
          HashSet<Road> hashSetRoad = new HashSet<>();
          cityRoads.put(cityStart, hashSetRoad);
        }

        // verifie l'existence de cityDest
        if (!cityRoads.containsKey(cityDest)) {
          HashSet<Road> hashSetRoad = new HashSet<>();
          cityRoads.put(cityDest, hashSetRoad);
        }

        // on ajoute la road dans cityStart
        cityRoads.get(cityStart).add(road);

        // ajoute route inverse à cityDest
        Road reverseRoad = new Road(cityDest, cityStart);
        cityRoads.get(cityDest).add(reverseRoad);

      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Méthode qui calcule l'itinéraire entre deux villes avec le moins de routes possible Utiliser la
   * méthode de BFS
   *
   * @param depart      le départ de l'itinéraire
   * @param destination la destination de l'itinéraire
   */
  public void calculerItineraireMinimisantNombreRoutes(String depart, String destination) {

    if(!cityName.containsKey(depart) || !cityName.containsKey(destination)){
      System.out.println("Depart ou destination n'existe pas");
      return;
    }
    Map<City, Road> predecessor = new HashMap<>();
    City start = cityName.get(depart);

    City dest = cityName.get(destination);

    boolean find = start.equals(dest);

    fileBfs.add(start);
    isVisitBfs.add(start);

    while (!fileBfs.isEmpty() && !find) {
      City visit = fileBfs.removeFirst();

      if (cityRoads.get(visit) == null) {
        continue;
      } else {
        for (Road road : cityRoads.get(visit)) {
          City goingTo = road.getCityDest();
          if (!isVisitBfs.contains(goingTo)) {
            fileBfs.add(goingTo);
            isVisitBfs.add(goingTo);
            predecessor.put(goingTo, road); // Enregistrer le prédécesseur
          }
          find = cityName.get(destination).getName().equals(goingTo.getName());
        }
      }
    }

    ArrayList<Road> chemin = new ArrayList<>();
    City actuel = dest;

    // Reconstruire le chemin en utilisant les prédécesseurs
    while (predecessor.containsKey(actuel)) {
      Road predRoad = predecessor.get(actuel);
      chemin.add(predRoad);
      actuel = predRoad.getCityStart();
    }

    Collections.reverse(chemin); // Inverser le chemin pour qu'il soit du départ à la destination

    affichage(chemin, depart, destination);
  }


  private void affichage(ArrayList <Road> chemin,String depart, String arriver){
    double totalDistance = 0;

    for (int i = 0; i < chemin.size(); i++) {
      City currentCity = chemin.get(i).getCityStart();
      City nextCity = chemin.get(i).getCityDest();
      totalDistance = totalDistance + distance(currentCity.getLatitude(), currentCity.getLongitude(),
          nextCity.getLatitude(), nextCity.getLongitude());;
    }

    System.out.println(
        "Itineraire de " + depart + " a " + arriver + " : " + chemin.size()
            + " routes et " + totalDistance + " kms");


    for (int i = 0; i < chemin.size(); i++) {
      City currentCity = chemin.get(i).getCityStart();
      City nextCity = chemin.get(i).getCityDest();
      double distance = distance(currentCity.getLatitude(), currentCity.getLongitude(),
          nextCity.getLatitude(), nextCity.getLongitude());

      System.out.println(
          currentCity.getName() + " -> " + nextCity.getName() + " (" + String.format("%.2f", distance) + " km)");
    }
  }


  /**
   * Méthode qui calcule l'itinéraire entre deux villes avec le moins de kilomètres Utiliser la
   * méthode de Dijkstra
   *
   * @param depart      le départ de l'itinéraire
   * @param destination la destination de l'itinéraire
   */
  public void calculerItineraireMinimisantKm(String depart, String destination) {
  }
}




