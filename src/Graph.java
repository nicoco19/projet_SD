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
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {
  private Map<City,HashSet<Road>> cityRoads; // map contenant une ville avec toutes ses routes sortantes
  private Map<String,City> cityName;
  private HashMap<Integer, City> citiesId;

  public Graph(File cities, File roads) {
    citiesId = new HashMap<Integer,City>();
    cityName = new HashMap<String,City>();

    String ligne;
    String[] tableLigne;

    try(FileReader fileReader = new FileReader(cities);

        BufferedReader bufferedReader =  new BufferedReader(fileReader)){

      while((ligne = bufferedReader.readLine()) != null){
        tableLigne = ligne.split(",");

        int id = Integer.parseInt(tableLigne[0]);
        String name = tableLigne[1];
        double longitude = Double.parseDouble(tableLigne[2]);
        double latitude = Double.parseDouble(tableLigne[3]);
        City city = new City(id,name,longitude,latitude);
        citiesId.put(id,city);
        cityName.put(name,city);
      }
    }catch(IOException e){
      throw new RuntimeException();
    }

    try(FileReader fileReader = new FileReader(roads);
        BufferedReader bufferedReader =  new BufferedReader(fileReader)){

      cityRoads = new HashMap<>();

      while((ligne = bufferedReader.readLine()) != null){

        tableLigne = ligne.split(",");

        int cityStartId = Integer.parseInt(tableLigne[0]);
        int cityDestId = Integer.parseInt(tableLigne[1]);
        City cityStart = citiesId.get(cityStartId);
        City cityDest = citiesId.get(cityDestId);

        Road road = new Road(cityStart, cityDest);

        if(cityRoads.get(road.getCityStart()) == null){
          cityRoads.put(road.getCityStart(),new HashSet<>());
        }
        cityRoads.get(road.getCityStart()).add(road);
      }
    }catch(IOException e){
      throw new RuntimeException();
    }
  }

  /**
   * Méthode qui calcule l'itinéraire entre deux villes avec le moins de routes possible
   * Utiliser la méthode de BFS
   * @param depart le départ de l'itinéraire
   * @param destination la destination de l'itinéraire
   */
  public void calculerItineraireMinimisantNombreRoutes(String depart, String destination) {

    Deque<City> fileBfs = new ArrayDeque<>(); // contient toutes les villes déjà visitées
    Set<City> isVisitBfs = new HashSet<>(); // contient les villes courantes dans la file du BFS

    Map<City, Road> chemin = new HashMap<City,Road>(); // Contient toutes les villes ainsi que leurs routes pour retracer le chemin

    City cityStart = cityName.get(depart); // on prend la station de départ
    City cityDest = cityName.get(destination); // et celle d'arrivée
    boolean find = false;

    // on ajoute à la queue et à l'ensemble la station
    fileBfs.add(cityStart);
    isVisitBfs.add(cityStart);

    while(!find){
      City actualCity = fileBfs.removeFirst(); // on supprime la ville à la queue pour l'utiliser comme ville courante

      for(Road road : cityRoads.get(actualCity)){ // on parcourt chacune des routes de la ville courante

        City cityEnd = road.getCityDest(); // on prend la ville d'arrivée pour la route traitée

        if(!isVisitBfs.contains(cityEnd)){ // cas de la ville jamais visitée
          isVisitBfs.add(cityEnd);

          fileBfs.add(cityEnd);
          chemin.put(cityEnd,road);
        }
        if(cityEnd.equals(cityDest)) find = true; // si on a trouvé toutes les villes de destination de la ville courante
      }
    }


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
