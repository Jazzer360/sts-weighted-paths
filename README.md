# Weighted Paths
A mod that helps evaluate map pathing to aid in finding the optimal path through each act.

## How does it work?
Internally, any time your path changes, the mod will look at every possible path through the act and create a value for each path based on the weights you have selected.

It then looks at every map node in every possible path, and saves the maximum value of all the paths that pass through that node.

This information is displayed on the map, allowing you to see which nodes will result in a path that has a high potential value.

### Store value
When determining the value of a store, the mod keeps track of each path's estimated gold at each step along the path. This factors in all of the gold related relics as well.

For example, if you have a membership card, any path that passes through a store will be valued as though your expected gold when reaching the store is doubled.  
Another example would be if you have the Ssserpent Head relic, all ? nodes you encounter on a path that contains a store will make that store's value higher because of the extra gold gained from the ? nodes.

