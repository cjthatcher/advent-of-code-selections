package com.dentist.other.year2021.day18;

import java.util.List;

import static com.dentist.other.year2021.day18.Orientation.*;

public class SnailNumber {

    SnailNumber left;
    SnailNumber right;
    int leftNum;
    int rightNum;
    SnailNumber parent;
    Orientation orientation;

    SnailNumber(SnailNumber left, SnailNumber right, int leftNum, int rightNum, SnailNumber parent) {
        this.left = left;
        this.right = right;
        this.leftNum = leftNum;
        this.rightNum = rightNum;
        this.parent = parent;
        this.orientation = ROOT;
    }

    public boolean isLeaf() {
        return (left == null && right == null);
    }

    public int depth() {
        if (parent == null) {
            return 0;
        } else {
            return parent.depth() + 1;
        }
    }

    // how many nodes are to the left of me?
    public int nodesToMyLeft() {
        if (left == null) {
            return 0;
        } else {
            return left.numberOfChildren() + 1;
        }
    }

    public int nodesToMyRight() {
        if (right == null) {
            return 0;
        } else {
            return right.numberOfChildren() + 1;
        }
    }

    // how many children do I have?
    public int numberOfChildren() {
        int children = 0;
        if (left != null) {
            children += left.numberOfChildren();
        }
        if (right != null) {
            children += right.numberOfChildren();
        }
        return children;
    }

    public String address() {
        StringBuilder address = new StringBuilder();
        if (parent != null) {
            address.append(parent.address());
            address.append(", ");

        }
        address.append(orientation.toString());
        return address.toString();
    }

    public String allDetails() {
        // go left go left go left
        StringBuilder sb = new StringBuilder();
        sb.append(nodeDetails());
        sb.append("\r\n");
        if (left != null) {
            sb.append(left.allDetails());
        } else {
            sb.append(leftNum).append(", ");
        }

        if (right != null) {
            sb.append(right.allDetails());
        } else {
            sb.append(rightNum).append(", ");
        }

        return sb.toString();
    }

    public String nodeDetails() {
        SnailNumber left = findNodeToMyLeft();
        SnailNumber right = findNodeToMyRight();
        StringBuilder sb = new StringBuilder();
        sb.append("I have a depth of ").append(depth()).append(". My address is ").append(address()).append(". my toString is ").append(printMe()).append("\r\n");
        sb.append("The node to my left is ").append(left == null ? "NULL" : left.printMe()).append("\r\n");
        sb.append("The node to my right is ").append(right == null ? "NULL" : right.printMe()).append("\r\n");
        return sb.toString();
    }

    public static SnailNumber fromString(String s) {
        // if it doesn't open with a '[', things are wrong.
        char[] chars = s.toCharArray();
        if (chars.length == 0) {
            throw new IllegalArgumentException("Whoa, trying to make a snail number out of an empty String");
        } else if (chars[0] != '[') {
            throw new IllegalArgumentException("Tried to open a SnailNumber without a brace.");
        }

        SnailNumberBuilder builder = new SnailNumberBuilder();

        // The inner left is a snail number
        if (chars[1] == '[') {
            SnailNumber left = fromString(s.substring(1));
            builder.withLeftSnailNumber(left);
        }

        // inner left is a number
        if (Character.isDigit(chars[1])) {
            int leftNumber = findNextInteger(s, 0);
            builder.withLeftInteger(leftNumber);
        }

        // I need to know where the right side of me starts.
        // So, I iterate till I find a comma that's at my same level.
        // We increment some counter when we find a '['
        // We decrement that counter when we find a ']'
        // We return when we find a comma when counter == 0;

        int commaLocation = findAppropriateComma(s, chars);

        if (commaLocation == -1) {
            throw new IllegalArgumentException("Tried to find the next comma, but couldn't. Huh.");
        }

        // Right node is a SnailNumber...
        if (chars[commaLocation + 1] == '[') {
            SnailNumber right = fromString(s.substring(commaLocation + 1));
            builder.withRightSnailNumber(right);
        }

        // Right node is a freaking integer...
        if (Character.isDigit(chars[commaLocation + 1])) {
            int rightNumber = findNextInteger(s, commaLocation);
            builder.withRightInteger(rightNumber);
        }

        SnailNumber number = builder.build();
        number.initialize();
        return number;
    }

    private void initialize() {
        if (left != null) {
            left.parent = this;
            left.orientation = LEFT;
        }

        if (right != null) {
            right.parent = this;
            right.orientation = RIGHT;
        }
    }

    private static int findNextInteger(String s, int startLocation) {
        char[] chars = s.toCharArray();
        int length = 1;
        if (Character.isDigit(chars[startLocation + 2])) {
            length = 2;
        }
        return Integer.parseInt(s.substring(startLocation + 1, startLocation + 1 + length));
    }

    private static int findAppropriateComma(String s, char[] chars) {
        int commaLocation = -1;
        int depth = 0;
        for (int i = 1; i < s.length(); i++) {
            if (chars[i] == '[') {
                depth++;
            } else if (chars[i] == ']') {
                depth--;
            } else if (chars[i] == ',') {
                if (depth == 0) {
                    commaLocation = i;
                    break;
                }
            }
        }
        return commaLocation;
    }

    public String printMe() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (left != null) {
            sb.append(left.printMe());
        } else {
            sb.append(leftNum);
        }
        sb.append(",");
        if (right != null) {
            sb.append(right.printMe());
        } else {
            sb.append(rightNum);
        }
        sb.append("]");
        return sb.toString();
    }

    public SnailNumber add(SnailNumber right) {
        SnailNumber left = this;
        left.orientation = LEFT;
        right.orientation = RIGHT;
        SnailNumberBuilder b = new SnailNumberBuilder();
        b.withLeftSnailNumber(left);
        b.withRightSnailNumber(right);
        SnailNumber parent = b.build();
        left.parent = parent;
        right.parent = parent;
        return parent;
    }

    public long magnitude() {
        long leftMag = 3 * (left == null ? leftNum : left.magnitude());
        long rightMag = 2 * (right == null ? rightNum : right.magnitude());
        return leftMag + rightMag;
    }

    public static SnailNumber addAll(List<String> strings) {
        if (strings.isEmpty()) {
            throw new IllegalArgumentException("Fetchers tried to add a bunch of non existent numbers together.");
        }
        SnailNumber result = SnailNumber.fromString(strings.getFirst());
        result = result.reduce();
        for (String s : strings.subList(1, strings.size())) {
            SnailNumber next = SnailNumber.fromString(s);
            result = result.add(next).reduce();
        }

        return result;
    }

    public SnailNumber reduce() {
        SnailNumber firstExploder = findFirstExploder(this);
        if (firstExploder != null) {
            firstExploder.explode();
            return reduce();
        }

        SnailNumber firstSplit = findFirstSplit(this);
        if (firstSplit != null) {
            firstSplit.split();
            return reduce();
        }

        return this;
    }

    private void split() {
        if (leftNum > 9) {
            int bigNumber = leftNum;

            SnailNumberBuilder b = new SnailNumberBuilder();
            b.withLeftInteger(Math.floorDiv(bigNumber, 2));
            b.withRightInteger(Math.ceilDiv(bigNumber, 2));
            SnailNumber leftie = b.build();
            leftie.parent = this;
            leftie.orientation = LEFT;

            left = leftie;
            leftNum = 0;
        } else if (rightNum > 9) {
            int bigNumber = rightNum;

            SnailNumberBuilder b = new SnailNumberBuilder();
            b.withLeftInteger(Math.floorDiv(bigNumber, 2));
            b.withRightInteger(Math.ceilDiv(bigNumber, 2));
            SnailNumber rightie = b.build();
            rightie.parent = this;
            rightie.orientation = RIGHT;

            right = rightie;
            rightNum = 0;
        }
    }

    private void explode() {
        if (!isLeaf()) {
            throw new IllegalStateException("Tried to explode a node that wasn't a leaf.");
        }

        // am I right or left?
        SnailNumber leftNode = findNodeToMyLeft();

        // if it's my parent, I use the right node.
        // if it's not my parent, It must have a left node.
        if (leftNode != null) {
            // If the right is a simple int, we'll add to it.
            if (leftNode.right == null) {
                leftNode.rightNum += leftNum;
            } else {
                // If the right is NOT a simple int, that means that this node is an ancestor, and I came from its right side.
                leftNode.leftNum += leftNum;
            }
        }

        SnailNumber rightNode = findNodeToMyRight();

        if (rightNode != null) {
            // If the left is NOT a snailNumber...
            if (rightNode.left == null) {
                rightNode.leftNum += rightNum;
            } else {
                // Otherwise this is one of my ancestors, and I came from their left line, so we have to add to the right.
                rightNode.rightNum += rightNum;
            }
        }

        if (orientation == RIGHT) {
            parent.right = null;
            parent.rightNum = 0;
        } else if (orientation == LEFT) {
            parent.left = null;
            parent.leftNum = 0;
        }

        parent = null;
    }

    private static SnailNumber findFirstExploder(SnailNumber snailNumber) {
        if (snailNumber.depth() == 4) {
            return snailNumber;
        } else {
            if (snailNumber.left != null) {
                SnailNumber exploder = findFirstExploder(snailNumber.left);
                if (exploder != null) {
                    return exploder;
                }
            }
            if (snailNumber.right != null) {
                SnailNumber exploder = findFirstExploder(snailNumber.right);
                if (exploder != null) {
                    return exploder;
                }
            }
        }
        return null;
    }

    private static SnailNumber findFirstSplit(SnailNumber snailNumber) {
        if (snailNumber.left != null) {
            SnailNumber splitter = findFirstSplit(snailNumber.left);
            if (splitter != null) {
                return splitter;
            }
        }
        if (snailNumber.leftNum > 9) {
            return snailNumber;
        }

        if (snailNumber.right != null) {
            SnailNumber splitter = findFirstSplit(snailNumber.right);
            if (splitter != null) {
                return splitter;
            }
        }

        if (snailNumber.rightNum > 9) {
            return snailNumber;
        }

        return null;
    }


    private SnailNumber findNodeToMyLeft() {
        // step 1: Find a suitable parent (the first parent to whom I am not a left descendant)

        // step 2: Find the rightmost descendant of that suitable parent
        SnailNumber suitableParent = findParentToWhomIAmRight(this);
        if (suitableParent != null) {
            return findRightmostLeftDescendant(suitableParent);
        } else {
            return null;
        }
    }

    private static SnailNumber findParentToWhomIAmRight(SnailNumber child) {
        if (child.orientation == ROOT) {
            return null;
        }
        if (child.orientation == RIGHT) {
            return child.parent;
        } else {
            return findParentToWhomIAmRight(child.parent);
        }
    }

    private static SnailNumber findRightmostLeftDescendant(SnailNumber parent) {
        // left is a normal number, this is the best descendant we've got
        if (parent.left == null) {
            return parent;
        } else {
            return findRightmostDescendant(parent.left);
        }
    }

    private static SnailNumber findRightmostDescendant(SnailNumber parent) {
        if (parent.right == null) {
            return parent;
        } else {
            return findRightmostDescendant(parent.right);
        }
    }


    private SnailNumber findNodeToMyRight() {
        // step 1: Find a suitable parent (the first parent to whom I am not a right descendant)

        // step 2: Find the leftmost descendant of that suitable parent
        SnailNumber suitableParent = findParentToWhomIAmLeft(this);
        if (suitableParent != null) {
            return findLeftmostRightDescendant(suitableParent);
        } else {
            return null;
        }
    }

    private static SnailNumber findParentToWhomIAmLeft(SnailNumber child) {
        if (child.orientation == ROOT) {
            return null;
        }
        if (child.orientation == LEFT) {
            return child.parent;
        } else {
            return findParentToWhomIAmLeft(child.parent);
        }
    }

    private static SnailNumber findLeftmostRightDescendant(SnailNumber parent) {
        // right is a normal number, this is the best descendant we've got
        if (parent.right == null) {
            return parent;
        } else {
            return findLeftmostDescendant(parent.right);
        }
    }

    private static SnailNumber findLeftmostDescendant(SnailNumber parent) {
        if (parent.left == null) {
            return parent;
        } else {
            return findLeftmostDescendant(parent.left);
        }

    }
}

enum Orientation {
    LEFT, RIGHT, ROOT
}

class SnailNumberBuilder {
    SnailNumber left;
    SnailNumber right;
    int leftNum;
    int rightNum;

    SnailNumberBuilder() {
    }

    public SnailNumberBuilder withLeftSnailNumber(SnailNumber left) {
        this.left = left;
        return this;
    }

    public SnailNumberBuilder withRightSnailNumber(SnailNumber right) {
        this.right = right;
        return this;
    }

    public SnailNumberBuilder withLeftInteger(int left) {
        this.leftNum = left;
        return this;
    }

    public SnailNumberBuilder withRightInteger(int right) {
        this.rightNum = right;
        return this;
    }

    public SnailNumber build() {
        return new SnailNumber(left, right, leftNum, rightNum, null);
    }

}
