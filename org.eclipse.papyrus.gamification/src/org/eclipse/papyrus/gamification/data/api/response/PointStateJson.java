package org.eclipse.papyrus.gamification.data.api.response;

import java.util.List;

public class PointStateJson {
	List<PointElementJson> PointConcept;

	public List<PointElementJson> getPointConcept() {
		return PointConcept;
	}

	public int getGoldCoins() {
		if (PointConcept == null) {
			return 0;
		}
		for (PointElementJson element : PointConcept) {
			if ("gold coins".contentEquals(element.getName())) {
				return element.getScore();
			}
		}
		return -1;
	}

	public int getPoints() {
		if (PointConcept == null) {
			return 0;
		}
		for (PointElementJson element : PointConcept) {
			if ("points".contentEquals(element.getName())) {
				return element.getScore();
			}
		}
		return -1;
	}
}
