package model;

import java.util.ArrayList;
import java.util.List;

public class QuizCategory {
	private String name;
	private QuizCategory parent;
	private List<QuizCategory> siblings;
	private List<QuizCategory> children;
	
	public QuizCategory(String name, QuizCategory parent) {
		this.name = name;
		this.parent = parent;
		siblings = new ArrayList<QuizCategory>();
		children = new ArrayList<QuizCategory>();
	}
	
	public String getName() {
		return name;
	}

	public QuizCategory getParent() {
		return parent;
	}

	public List<QuizCategory> getSiblings() {
		return siblings;

	}

	public List<QuizCategory> getChildren() {
		return children;
	}

	public void addChild(QuizCategory category) {
		children.add(category);
	}

}
