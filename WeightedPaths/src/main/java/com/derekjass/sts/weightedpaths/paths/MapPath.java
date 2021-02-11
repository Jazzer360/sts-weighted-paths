package com.derekjass.sts.weightedpaths.paths;

import com.derekjass.sts.weightedpaths.WeightedPaths;
import com.derekjass.sts.weightedpaths.helpers.RelicTracker;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.helpers.SeedHelper;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import io.sentry.Breadcrumb;
import io.sentry.Sentry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class MapPath extends ArrayList<MapRoomNode> implements Comparable<MapPath> {

    private static final Logger logger = LogManager.getLogger(MapPath.class.getName());

    private double value = 0.0f;

    private MapPath(MapRoomNode room) {
        add(room);
    }

    private MapPath(MapEdge edge) {
        add(edge);
    }

    private static List<MapPath> starterPaths() {
        List<MapRoomNode> firstFloor = CardCrawlGame.dungeon.getMap().get(0);
        return firstFloor.stream()
                .filter(MapRoomNode::hasEdges)
                .map(MapPath::new)
                .collect(Collectors.toList());
    }

    public static List<MapPath> generateAll() throws UnexpectedStateException {
        addSentryBreadcrumb("Begin path generation.");
        addSentryBreadcrumb();
        List<MapPath> paths = new ArrayList<>();
        if (CardCrawlGame.dungeon instanceof TheEnding) {
            addSentryBreadcrumb("In the ending, so don't generate anything.");
            return paths;
        } else if (!AbstractDungeon.firstRoomChosen) {
            addSentryBreadcrumb("Act is fresh, so generate starter paths.");
            paths = starterPaths();
        } else if (AbstractDungeon.getCurrMapNode() == null) {
            throw new UnexpectedStateException("AbstractDungeon current map node is null.");
        } else if (AbstractDungeon.getCurrMapNode().y < maxFloor()) {
            addSentryBreadcrumb("Generating from current room.");
            if (!AbstractDungeon.getCurrMapNode().hasEdges()) {
                throw new UnexpectedStateException("Current map node has no edges.");
            }
            for (MapEdge edge : AbstractDungeon.getCurrMapNode().getEdges()) {
                paths.add(new MapPath(edge));
            }
        } else {
            addSentryBreadcrumb("Floor is not eligible for path generation.");
            return paths;
        }
        generateRemaining(paths);
        logger.info("Total paths found: " + paths.size());
        Sentry.clearBreadcrumbs();
        return paths;
    }

    private static void generateRemaining(List<MapPath> paths) throws UnexpectedStateException {
        List<MapPath> newPaths = new ArrayList<>();
        Iterator<MapPath> iter = paths.iterator();
        while (iter.hasNext()) {
            MapPath path = iter.next();
            MapRoomNode lastRoom = path.last();
            if (lastRoom == null) {
                throw new UnexpectedStateException("During path generation, last node in path returned null.");
            } else if (lastRoom.y == maxFloor()) {
                continue;
            } else if (!lastRoom.hasEdges()) {
                addSentryBreadcrumb("Removing path. Last room in path has no edges.");
                iter.remove();
                continue;
            }
            for (int i = 1; i < lastRoom.getEdges().size(); i++) {
                MapPath newPath = (MapPath) path.clone();
                newPath.add(lastRoom.getEdges().get(i));
                newPaths.add(newPath);
            }
            path.add(lastRoom.getEdges().get(0));
        }
        paths.addAll(newPaths);
        if (paths.stream().anyMatch(path -> path.last().y < maxFloor())) {
            generateRemaining(paths);
        }
    }

    private static int maxFloor() {
        return AbstractDungeon.MAP_HEIGHT - 2;
    }

    private static void addSentryBreadcrumb(String note) {
        logger.info(note);
        Breadcrumb crumb = new Breadcrumb();
        crumb.setCategory("map-generation");
        crumb.setMessage(note);
        Sentry.addBreadcrumb(crumb);
    }

    private static void addSentryBreadcrumb() {
        Breadcrumb crumb = new Breadcrumb();
        crumb.setCategory("map-generation");
        crumb.setData("floor", String.valueOf(AbstractDungeon.floorNum));
        crumb.setData("act", CardCrawlGame.dungeon.getClass().getSimpleName());
        crumb.setData("room", AbstractDungeon.getCurrMapNode() == null ?
                "NULL" : AbstractDungeon.getCurrMapNode().room.getClass().getSimpleName());
        crumb.setData("seed", SeedHelper.getString(Settings.seed));
        crumb.setData("character", AbstractDungeon.player.getClass().getSimpleName());
        Sentry.addBreadcrumb(crumb);
    }

    private void add(MapEdge edge) {
        MapRoomNode room = CardCrawlGame.dungeon.getMap().get(edge.dstY).get(edge.dstX);
        add(room);
    }

    private MapRoomNode last() {
        return get(size() - 1);
    }

    public void valuate() {
        double summedValue = 0.0;
        double estimatedGold = AbstractDungeon.player.gold;
        boolean hasMaw = RelicTracker.hasMaw;
        for (MapRoomNode room : this) {
            String roomSymbol = room.getRoomSymbol(true);
            if (!RelicTracker.hasEcto) {
                estimatedGold += (hasMaw ? 12.0 : 0.0);
            }
            switch (roomSymbol) {
                case "M":
                    summedValue += WeightedPaths.weights.get(roomSymbol);
                    if (!RelicTracker.hasEcto) {
                        estimatedGold += 15.0 + (RelicTracker.hasIdol ? 3.7 : 0.0);
                    }
                    break;
                case "?":
                    summedValue += WeightedPaths.weights.get(roomSymbol);
                    if (!RelicTracker.hasEcto) {
                        estimatedGold += (RelicTracker.hasFace ? 50.0 : 0.0);
                    }
                    break;
                case "E":
                    summedValue += WeightedPaths.weights.get(roomSymbol);
                    if (!RelicTracker.hasEcto) {
                        estimatedGold += 30.0 + (RelicTracker.hasIdol ? 7.8 : 0.0);
                    }
                    break;
                case "R":
                    summedValue += WeightedPaths.weights.get(roomSymbol);
                    break;
                case "$":
                    WeightedPaths.storeGold.put(room,
                            Math.max(WeightedPaths.storeGold.getOrDefault(room, 0.0), estimatedGold));
                    summedValue += estimatedGold / 100 / (RelicTracker.hasMembership ? 0.5 : 1.0)
                            / (RelicTracker.hasCourier ? 0.8 : 1.0) * WeightedPaths.weights.get(roomSymbol);
                    estimatedGold = 0.0;
                    hasMaw = false;
                    break;
                case "T":
                    if (!RelicTracker.hasEcto) {
                        estimatedGold += 18.4;
                    }
                    break;
            }
        }
        this.value = summedValue;
    }

    public double getValue() {
        return value;
    }

    public boolean hasEmerald() {
        return stream().anyMatch(room -> room.hasEmeraldKey);
    }

    @Override
    public int compareTo(MapPath o) {
        return Double.compare(value, o.value);
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("Value: " + value + ", Nodes:");
        for (MapRoomNode room : this) {
            out.append(" ").append(room.getRoomSymbol(true));
        }
        return out.toString();
    }
}