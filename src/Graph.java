package src;

import static src.Util.distance;
import java.io.BufferedReader;
import java.io.File;
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

  public Graph(File cities, File roads) {
    cityRoads = new HashMap<City, HashSet<Road>>();
    citiesId = new HashMap<Integer, City>();
    cityName = new HashMap<String, City>();

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

        if (!cityRoads.containsKey(cityStart)) {
          HashSet<Road> hashSetRoad = new HashSet();
          cityRoads.put(cityStart, hashSetRoad);
        }

        cityRoads.get(cityStart).add(road);

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
    // on cré un map pour reteenir le prédécesseur de la ville
    Map<City, Road> predecessor = new HashMap<>();
    // la ville de départ
    City start = cityName.get(depart);
    // la ville d'arriver
    City dest = cityName.get(destination);
   // boolean qui va définir si l'élément est trouvé ou pas
    boolean find = start.equals(dest);
   // est la file de l'algorithme BFS
    Deque<City> fileBfs = new ArrayDeque<City>();
    // est un HashSet qui va retenir les ville ou on est passé
    Set<City> isVisitBfs = new HashSet<City>();
    // on ajoute la ville start dans la file BFS
    fileBfs.add(start);
    // on ajoute la ville start dans la liste des villes visitées
    isVisitBfs.add(start);

    // on va continuer à parcourir les éléments jusqu'au moment ou on a trouvé la ville
    // ou si la file BFS est vide donc on a visité tous les éléments
    while (!fileBfs.isEmpty() && !find) {
      City visit = fileBfs.removeFirst();

      if (cityRoads.get(visit) == null) {
        continue;
      } else {

        for (Road road : cityRoads.get(visit)) {
         System.out.println("la ville de départ est : " + road.getCityStart().getName());
         System.out.println("la ville d'arriver est : " + road.getCityDest().getName());

          City goingTo = road.getCityDest();

          if (!isVisitBfs.contains(goingTo)) {
            fileBfs.add(goingTo);
            isVisitBfs.add(goingTo);
            predecessor.put(goingTo, road);
          }
          // vérifie si l'élément est trouvé ou pas
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
    // inverse les éléments de chemin pour pouvoir reformer le chemin
    Collections.reverse(chemin);
    // affiche le chemin sous forme textuel
    affichage(chemin, depart, destination);
  }


  private void affichage(ArrayList <Road> chemin,String depart, String arriver){

    double totalDistance = 0;

    for (Road road : chemin) {
      City currentCity = road.getCityStart();
      City nextCity = road.getCityDest();
      double distance = distance(currentCity.getLatitude(), currentCity.getLongitude(),
          nextCity.getLatitude(), nextCity.getLongitude());
      totalDistance = totalDistance + distance;

      System.out.println(
          currentCity.getName() + " -> " + nextCity.getName() + " (" + distance + " km)");

    }

    System.out.println(
        "Trajet de " + depart + " a " + arriver + " : " + chemin.size()
            + " routes et " + totalDistance + " kms");
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




