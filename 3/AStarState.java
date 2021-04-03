/**
 * This class stores the basic state necessary for the A* algorithm to compute a
 * path across a map.  This state includes a collection of "open waypoints" and
 * another collection of "closed waypoints."  In addition, this class provides
 * the basic operations that the A* pathfinding algorithm needs to perform its
 * processing.
 **/

import java.util.HashMap;

public class AStarState
{
    /** This is a reference to the map that the A* algorithm is navigating. **/
    private Map2D map;

    // Инициализация данной карты
    private HashMap<Location, Waypoint> closed_points;
    private HashMap<Location, Waypoint> opened_points;

    /**
     * Initialize a new state object for the A* pathfinding algorithm to use.
     **/
    public AStarState(Map2D map) {
        if (map == null)
            throw new NullPointerException("map cannot be null");

        this.map = map;

        // Инициализация данных хеш-таблиц
        closed_points = new HashMap<>();
        opened_points = new HashMap<>();
    }

    /** Returns the map that the A* pathfinder is navigating. **/
    public Map2D getMap()
    {
        return map;
    }

    /**
     * This method scans through all open waypoints, and returns the waypoint
     * with the minimum total cost.  If there are no open waypoints, this method
     * returns <code>null</code>.
     **/
    public Waypoint getMinOpenWaypoint()
    {
        if (opened_points.size() == 0) {
            return null;
        }

        float minimum_cost = 100000f;
        Waypoint minimum_point = null;

        for(Waypoint value: opened_points.values()) {
            if (value.getPreviousCost() <= minimum_cost) {
                minimum_cost = value.getPreviousCost();
                minimum_point = value;
            }
        }
        return minimum_point;
    }

    /**
     * This method adds a waypoint to (or potentially updates a waypoint already
     * in) the "open waypoints" collection.  If there is not already an open
     * waypoint at the new waypoint's location then the new waypoint is simply
     * added to the collection.  However, if there is already a waypoint at the
     * new waypoint's location, the new waypoint replaces the old one <em>only
     * if</em> the new waypoint's "previous cost" value is less than the current
     * waypoint's "previous cost" value.
     **/
    public boolean addOpenWaypoint(Waypoint newWP) {
        Location loc = newWP.getLocation();

        // Добавляем новую локацию
        if (opened_points.get(loc) == null) {
            opened_points.put(loc, newWP);
            return true;
        } else {
            // Если стоимость нового маршрута меньше той, которая в хеше, то заменяем
            if (opened_points.get(loc).getPreviousCost() > newWP.getPreviousCost()) {
                opened_points.put(loc, newWP);
                return true;
            }
            return false;
        }
    }

    /** Returns the current number of open waypoints. **/
    public int numOpenWaypoints() {
        return opened_points.size();
    }

    /**
     * This method moves the waypoint at the specified location from the
     * open list to the closed list.
     **/
    public void closeWaypoint(Location loc) {
        // Получает объект класса Way_point из хеш-таблицы
        Waypoint value = opened_points.get(loc);
        opened_points.remove(loc);

        closed_points.put(loc, value);
    }

    /**
     * Returns true if the collection of closed waypoints contains a waypoint
     * for the specified location.
     **/
    public boolean isLocationClosed(Location loc) {
        Waypoint val = closed_points.get(loc);
        return val != null;
    }
}

