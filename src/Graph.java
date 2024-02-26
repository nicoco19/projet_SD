package src;

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {
  private Map<City,HashSet<Road>> cityRoads;
  private Deque<City> fileBfs;
  private Set<City> isVisitBfs;

  public Graph(File cities, File roads) {
    fileBfs = new ArrayDeque<City>();
    isVisitBfs = new HashSet<City>();
    cityRoads = new HashMap<City,HashSet<Road>>();

    String[] tabCity = new String[3];
    try {
      BufferedReader baladeur = new BufferedReader(new FileReader(roads));
      String line;
      while ((line = baladeur.readLine()) != null){
        tabCity = line.split(",");

      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Méthode qui calcule l'itinéraire entre deux villes avec le moins de routes possible
   * Utiliser la méthode de BFS
   * @param depart le départ de l'itinéraire
   * @param destination la destination de l'itinéraire
   */
  public void calculerItineraireMinimisantNombreRoutes(String depart, String destination) {
    
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
