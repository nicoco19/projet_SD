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
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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

    // on ajoute à la queue et à l'ensemble la station
    fileBfs.add(cityStart);
    isVisitBfs.add(cityStart);

    boolean find = false;
    while(!find){
      City actualCity = fileBfs.removeFirst(); // on supprime la ville à la queue pour l'utiliser comme ville courante

      if (cityRoads.get(actualCity) == null) {
        continue;
      }else {
        for(Road road : cityRoads.get(actualCity)){ // on parcourt chacune des routes de la ville courante

          City cityEnd = road.getCityDest(); // on prend la ville d'arrivée pour la route traitée

          if(!isVisitBfs.contains(cityEnd)){ // cas de la ville jamais visitée
            isVisitBfs.add(cityEnd);

            fileBfs.add(cityEnd);
            chemin.put(cityEnd,road);
          }
          if(cityEnd.getName().equals(cityDest.getName())) find = true; // si on a trouvé toutes les villes de destination de la ville courante
        }
      }

    }

    ArrayList<Road> chemins = new ArrayList<>();
    City villeActuelle = cityName.get(destination);

    // Reconstruire le chemin en utilisant les prédécesseurs
    while (chemin.containsKey(villeActuelle)) {
      Road predRoad = chemin.get(villeActuelle);
      chemins.add(predRoad);
      villeActuelle = predRoad.getCityStart();
    }

    Collections.reverse(chemins); // Inverser le chemin pour qu'il soit du départ à la destination

    affichage(chemins, depart, destination);
  }

  /**
   * Méthode qui calcule l'itinéraire entre deux villes avec le moins de kilomètres
   * Utiliser la méthode de Dijkstra
   * @param depart le départ de l'itinéraire
   * @param destination la destination de l'itinéraire
   */
  public void calculerItineraireMinimisantKm(String depart, String destination) {

    TreeSet<City> etiquetteProvisoire = new TreeSet<>(Comparator.comparing(City::getTempsEtiquetteProvisoire).thenComparing(City::getName));
    // Pour l'étiquette provisoire, on doit les trier par ordre de temps d'atteinte, ce qui permettra de traiter le plus petit l'un après l'autre

    Set<City> etiquetteDefinitive = new HashSet<>();
    HashMap<City,Road> chemin = new HashMap<>();

    City cityStart = cityName.get(depart);
    City cityDest = cityName.get(destination);

    cityStart.setTempsEtiquetteProvisoire(0); // on met à 0 la valeur de son temps (départ)
    etiquetteProvisoire.add(cityStart);

    while (!etiquetteProvisoire.isEmpty()){
      City cityActual = etiquetteProvisoire.removeFirst(); // on supprime et renvoie le premier élément

      if (cityRoads.get(cityActual) == null) {
        continue;
      }else{
        for(Road road : cityRoads.get(cityActual)){ // on parcourt toutes les routes sortantes de la ville actuelle
          City cityEnd = road.getCityDest(); // pour chaque route, on prend la ville de destination

          // ville inconnue
          int tempsProvisoire = cityActual.getTempsEtiquetteProvisoire() + road.getDuree();

          if(!etiquetteProvisoire.contains(cityEnd)){
            cityEnd.setTempsEtiquetteProvisoire(tempsProvisoire);
            etiquetteProvisoire.add(cityEnd);
            chemin.put(cityEnd,road); // on retient pour la ville, sa route
          }else {
            if(cityEnd.getTempsEtiquetteProvisoire() > tempsProvisoire){
              etiquetteProvisoire.remove(cityEnd);
              cityEnd.setTempsEtiquetteProvisoire(tempsProvisoire);
              etiquetteProvisoire.add(cityEnd);
              chemin.put(cityEnd,road);
            }
          }
        }
      }

    }

    ArrayList<Road> chemins = new ArrayList<>();
    City villeActuelle = cityName.get(destination);

    // Reconstruire le chemin en utilisant les prédécesseurs
    while (chemin.containsKey(villeActuelle)) {
      Road predRoad = chemin.get(villeActuelle);
      chemins.add(predRoad);
      villeActuelle = predRoad.getCityStart();
    }

    Collections.reverse(chemins); // Inverser le chemin pour qu'il soit du départ à la destination

    affichage(chemins, depart, destination);

    affichage(chemins,depart,destination);
  }

  private void affichage(ArrayList <Road> chemin,String depart, String arriver){
    double totalDistance = 0;
    //Collections.reverse(chemin);
    for (int i = 0; i < chemin.size(); i++) {
      City currentCity = chemin.get(i).getCityStart();
      City nextCity = chemin.get(i).getCityDest();
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
}
