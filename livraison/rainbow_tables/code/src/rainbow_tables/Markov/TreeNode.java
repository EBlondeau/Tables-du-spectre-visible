package rainbow_tables.Markov;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class TreeNode {

    private Character c;
    private TreeNode parent;
    private List<TreeNode> children;
    private double capital;

    public TreeNode(Character c, double capital) {
        this.c = c;
        this.capital = capital;
        this.parent = null;
        children = new ArrayList<TreeNode>();
    }

    public TreeNode addChild(TreeNode node) {

        node.setParent(this);
        this.children.add(node);
        return this;
    }

    public TreeNode addChildren(List<TreeNode> children) {
        for (int i = 0; i < children.size(); i++) {
            children.get(i).parent = this;
        }
        this.children.addAll(children);
        return this;
    }

    public Character getChar() {
        return this.c;
    }

    public double getCapital() {
        return this.capital;
    }

    public TreeNode getLastChildren() {
        return this.children.get(children.size() - 1);
    }

    public List<TreeNode> getChildren() {
        return this.children;
    }

    public void setCapital(double capital) {
        this.capital = capital;
    }

    public TreeNode getParent() {
        return this.parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

}