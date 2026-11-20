package sk.tuke.kpi.oop.game;

public enum Direction {
    EAST(1, 0, 270f),
    NORTH(0, 1, 0f),
    WEST(-1, 0, 90f),
    SOUTH(0, -1, 180f),
    SOUTH_EAST(1, -1, 225f),
    SOUTH_WEST(-1, -1, 135f),
    NORTH_EAST(1, 1, 315f),
    NORTH_WEST(-1, 1, 45f),
    // alias names without underscores to satisfy valueOf expectations
    NORTHEAST(1, 1, 315f),
    NORTHWEST(-1, 1, 45f),
    SOUTHEAST(1, -1, 225f),
    SOUTHWEST(-1, -1, 135f),
    NONE(0, 0, 0f);

    private final int dx;
    private final int dy;
    private final float rotation;

    Direction(int dx, int dy, float rotation) {
        this.dx = dx;
        this.dy = dy;
        this.rotation = rotation;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public float getAngle() {
        return rotation;
    }

    public Direction combine(Direction other) {
        return combine(this, other);
    }

    public static Direction fromVector(int dx, int dy) {
        if (dx == 0 && dy == 0) return NONE;
        int signDx = Integer.signum(dx);
        int signDy = Integer.signum(dy);
        
        Direction diagonal = getDiagonalDirection(signDx, signDy);
        if (diagonal != null) return diagonal;
        
        return findDirectionBySigns(signDx, signDy);
    }

    private static Direction getDiagonalDirection(int signDx, int signDy) {
        if (signDx == 1 && signDy == 1) return NORTHEAST;
        if (signDx == -1 && signDy == 1) return NORTHWEST;
        if (signDx == 1 && signDy == -1) return SOUTHEAST;
        if (signDx == -1 && signDy == -1) return SOUTHWEST;
        return null;
    }

    private static Direction findDirectionBySigns(int signDx, int signDy) {
        for (Direction dir : values()) {
            if (dir.dx == signDx && dir.dy == signDy) {
                return dir;
            }
        }
        return NONE;
    }

    public static Direction combine(Direction dir1, Direction dir2) {
        int dx = dir1.dx + dir2.dx;
        int dy = dir1.dy + dir2.dy;
        return fromVector(dx, dy);
    }

    public static Direction fromAngle(float angle) {
        float normalized = normalizeAngle(angle);
        return findClosestDirection(normalized);
    }

    private static float normalizeAngle(float angle) {
        float normalized = angle % 360;
        if (normalized < 0) normalized += 360;
        return normalized;
    }

    private static Direction findClosestDirection(float normalized) {
        Direction closest = NORTH;
        float minDiff = Math.abs(normalized - NORTH.rotation);

        for (Direction dir : values()) {
            if (shouldSkipDirection(dir)) continue;
            
            float diff = calculateAngleDifference(normalized, dir.rotation);
            if (diff < minDiff) {
                minDiff = diff;
                closest = dir;
            }
        }
        return closest;
    }

    private static boolean shouldSkipDirection(Direction dir) {
        if (dir == NONE) return true;
        return dir == NORTH_EAST || dir == NORTH_WEST || 
               dir == SOUTH_EAST || dir == SOUTH_WEST;
    }

    private static float calculateAngleDifference(float angle1, float angle2) {
        float diff = Math.abs(angle1 - angle2);
        if (diff > 180) diff = 360 - diff;
        return diff;
    }
}
