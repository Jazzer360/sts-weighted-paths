package com.derekjass.sts.weightedpaths.paths;

import com.derekjass.sts.weightedpaths.WeightedPaths;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MapPath extends LinkedList<MapRoomNode> implements Comparable<MapPath> {

    private static final Logger logger = LogManager.getLogger(MapPath.class.getName());

    private double value = 0.0f;

    public static List<MapPath> generateAll() {
        logger.info("Begin path generation.");
        List<MapPath> paths = new ArrayList<>();
        int floorNum = AbstractDungeon.floorNum;
        if (floorNum > 48 || (floorNum - 1) % 17 > 13) {
            return paths;
        }
        if (AbstractDungeon.getCurrMapNode() == null) {
            ArrayList<MapRoomNode> firstFloor = CardCrawlGame.dungeon.getMap().get(0);
            for (MapRoomNode room : firstFloor) {
                if (!room.hasEdges()) {
                    continue;
                }
                MapPath path = new MapPath();
                path.add(room);
                paths.add(path);
            }
        } else {
            MapPath path = new MapPath();
            path.add(AbstractDungeon.getCurrMapNode());
            paths.add(path);
        }
        generateRemaining(paths);
        logger.info("Total paths found: " + paths.size());
        return paths;
    }

    private static void generateRemaining(List<MapPath> paths) {
        List<MapPath> newPaths = new LinkedList<>();
        for (MapPath path : paths) {
            MapRoomNode lastRoom = path.peekLast();
            assert lastRoom != null;
            if (lastRoom.y == 13) {
                return;
            }
            ArrayList<MapEdge> edges = lastRoom.getEdges();
            if (edges.size() > 1) {
                for (int i = 1; i < edges.size(); i++) {
                    MapPath newPath = (MapPath) path.clone();
                    newPath.addRoomToPath(edges.get(i));
                    newPaths.add(newPath);
                }
            }
            path.addRoomToPath(edges.get(0));
        }
        paths.addAll(newPaths);
        generateRemaining(paths);
    }

    private void addRoomToPath(MapEdge edge) {
        MapRoomNode room = CardCrawlGame.dungeon.getMap().get(edge.dstY).get(edge.dstX);
        add(room);
    }

    public void valuate() {
        // TODO: allow for user customization of weights
        // TODO: factor estimated gold for store weights
        double summedValue = 0.0;
        double estimatedGold = AbstractDungeon.player.gold;
        boolean hasIdol = false, hasFace = false, hasMaw = false, hasMembership = false, hasCourier = false;
        for (MapRoomNode room : this) {
            String roomSymbol = room.getRoomSymbol(true);
            switch (roomSymbol) {
                case "M":
                    estimatedGold += 15.0 * (hasIdol ? 1.25 : 1.0);
                    summedValue += WeightedPaths.weights.get(roomSymbol);
                    break;
                case "?":
                    estimatedGold += hasFace ? 50.0 : 0.0;
                    summedValue += WeightedPaths.weights.get(roomSymbol);
                    break;
                case "E":
                    estimatedGold += 30.0 * (hasIdol ? 1.25 : 1.0);
                    summedValue += WeightedPaths.weights.get(roomSymbol);
                    break;
                case "R":
                    break;
                case "$":
                    break;
            }
            String roomType = room.getRoomSymbol(true);
            if (roomType.equals("$")) {
                summedValue += estimatedGold / 100 * WeightedPaths.weights.get(roomType);
                estimatedGold = 0.0;
            } else if (roomType.equals("E")){
                summedValue += WeightedPaths.weights.get(roomType);
            }
        }
        this.value = summedValue;
    }

    public double getValue() {
        return value;
    }

    @Override
    public int compareTo(MapPath o) {
        return Double.compare(value, o.value);
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("\nValue: " + value + "\nNodes:");
        for (MapRoomNode room : this) {
            out.append(" ").append(room.getRoomSymbol(true));
        }
        return out.toString();
    }
}