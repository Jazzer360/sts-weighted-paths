package com.derekjass.sts.weightedpaths.paths;

import com.derekjass.sts.weightedpaths.WeightedPaths;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MapPath extends LinkedList<MapRoomNode> implements Comparable<MapPath> {

    private static final Logger logger = LogManager.getLogger(MapPath.class.getName());

    private float value = 0.0f;

    public static List<MapPath> generateAll() {
        logger.info("Begin path generation.");
        List<MapPath> paths = new ArrayList<>();
        // TODO: handle boss floors
        if (AbstractDungeon.getCurrMapNode() == null) {
            ArrayList<MapRoomNode> firstFloor = CardCrawlGame.dungeon.getMap().get(0);
            logger.debug(firstFloor);
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
        generateAll(paths);
        logger.info("Total paths found: " + paths.size());
        WeightedPaths.roomValues.clear();
        for (MapPath path : paths) {
            path.valuate();
        }
        paths.sort(Collections.reverseOrder());
        logger.info("Paths evaluated and sorted.");
        return paths;
    }

    public static void generateAll(List<MapPath> paths) {
        MapRoomNode lastRoom = paths.get(0).peekLast();
        assert lastRoom != null;
        if (lastRoom.y == 13) {
            return;
        }
        List<MapPath> newPaths = new ArrayList<>();
        for (MapPath path : paths) {
            lastRoom = path.peekLast();
            assert lastRoom != null;
            ArrayList<MapEdge> edges = lastRoom.getEdges();
            if (edges.size() == 1) {
                MapEdge edge = edges.get(0);
                path.addRoomToPath(edge);
            } else {
                for (int j = 1; j < edges.size(); j++) {
                    MapPath newPath = (MapPath) path.clone();
                    newPath.addRoomToPath(edges.get(j));
                    newPaths.add(newPath);
                }
                path.addRoomToPath(edges.get(0));
            }
        }
        paths.addAll(newPaths);
        generateAll(paths);
    }

    private void addRoomToPath(MapEdge edge) {
        MapRoomNode room = CardCrawlGame.dungeon.getMap().get(edge.dstY).get(edge.dstX);
        add(room);
    }

    public void valuate() {
        // TODO: allow for user customization of weights
        // TODO: factor estimated gold for store weights
        float summedValue = 0.0f;
        for (MapRoomNode room : this) {
            String roomType = room.getRoomSymbol(true);
            summedValue += WeightedPaths.weights.get(roomType);
        }
        for (MapRoomNode room: this) {
            Float val = WeightedPaths.roomValues.get(room);
            if (val == null || val < summedValue) {
                WeightedPaths.roomValues.put(room, summedValue);
            }
        }
        this.value = summedValue;
    }

    @Override
    public int compareTo(MapPath o) {
        return Float.compare(value, o.value);
    }

    public void highlightOnMap() {

    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("Value: " + value + "\nNodes: ");
        for (MapRoomNode room : this) {
            out.append(room.getRoomSymbol(true)).append(" ");
        }
        return out.toString();
    }
}