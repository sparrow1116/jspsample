package com.sec.webs.classification.entity;

import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.TagNode;

public class DomNode {
	public List<DomNode> getNodeList() {
		return nodeList;
	}

	public void setNodeList(List<DomNode> nodeList) {
		this.nodeList = nodeList;
	}

	public TagNode getTagNode() {
		return tagNode;
	}

	public void setTagNode(TagNode tagNode) {
		this.tagNode = tagNode;
	}

	private List<DomNode> nodeList = new ArrayList<DomNode>();
	private TagNode tagNode;
}
