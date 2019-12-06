package day3;

public enum Direction {
    UP("U"),
    DOWN("D"),
    LEFT("L"),
    RIGHT("R");

    private String alias;
    private Direction(String s) {
        alias = s;
    }
    public String getAlias(Direction d) {
        return d.alias;
    }

    public static Direction getDirection(char alias) {
        if(alias == 'U') return Direction.UP;
        if(alias == 'D') return Direction.DOWN;
        if(alias == 'L') return Direction.LEFT;
        if(alias == 'R') return Direction.RIGHT;
        return null;
    }
}
