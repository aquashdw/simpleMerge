import java.awt.Color;
import java.util.*;

public class DP {

	private static ArrayList<Character> leftCharacters;
	private static ArrayList<Character> rightCharacters;

	private static int DPTable[][];

	private static ArrayList<Character> newLeftCharacters;
	private static ArrayList<Character> newRightCharacters;

	private static Vector<Integer> check1;
	private static Vector<Integer> check2;

	private static ArrayList<Highlight> leftHighlights;
	private static ArrayList<Highlight> rightHighlights;

	public static void setTexts(String leftText, String rightText) {
		leftCharacters = new ArrayList<Character>();
		rightCharacters = new ArrayList<Character>();

//		private static int DPTable[][];

		newLeftCharacters = new ArrayList<Character>();
		newRightCharacters = new ArrayList<Character>();

		check1 = new Vector<Integer>();
		check2 = new Vector<Integer>();

		leftHighlights = new ArrayList<Highlight>();
		rightHighlights = new ArrayList<Highlight>();
		
		leftCharacters.add('\n');
		rightCharacters.add('\n');
		loadChatacters(leftText, leftCharacters);
		loadChatacters(rightText, rightCharacters);

		leftCharacters.add('\n');
		rightCharacters.add('\n');

		makeDPTable();
		findLCS();
		updatearray();
	}

	public static ArrayList<Highlight> getLeftHighlights() {
		return leftHighlights;
	}

	public static String getNewLeftText() {
		return arrayListToString(newLeftCharacters);
	}

	public static ArrayList<Highlight> getRightHighlights() {
		return rightHighlights;
	}

	public static String getNewRightText() {
		return arrayListToString(newRightCharacters);
	}

	private static String arrayListToString(ArrayList<Character> list) {
		StringBuilder builder = new StringBuilder(list.size());
		for (Character ch : list) {
			builder.append(ch);
		}
		return builder.toString();
	}

	private static void loadChatacters(String text, ArrayList<Character> array) {

		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			array.add(ch);
		}
		array.add('\n');
	}

	private static void makeDPTable() {
		int length1 = leftCharacters.size();
		int length2 = rightCharacters.size();

		DPTable = new int[length1 + 1][length2 + 1];

		for (int i = 0; i <= length1; i++) {
			DPTable[i][0] = 0;
		}
		for (int j = 0; j <= length2; j++) {
			DPTable[0][j] = 0;
		}

		for (int i = 1; i <= length1; i++) {
			for (int j = 1; j <= length2; j++) {
				if (leftCharacters.get(i - 1) == rightCharacters.get(j - 1)) {
					DPTable[i][j] = DPTable[i - 1][j - 1] + 1;
				} else {
					DPTable[i][j] = Integer.max(DPTable[i][j - 1], DPTable[i - 1][j]);
				}
			}
		}
	}

	private static void findLCS() {
		int now_x = leftCharacters.size();
		int now_y = rightCharacters.size();

		while (true) {
			if (leftCharacters.get(now_x - 1) != rightCharacters.get(now_y - 1)) {
				if (DPTable[now_x - 1][now_y] > DPTable[now_x][now_y - 1]) {
					now_x = now_x - 1;
				} else {
					now_y = now_y - 1;
				}

				if (DPTable[now_x][now_y] == 0 || now_x == 0 || now_y == 0) {
					break;
				}
			} else {
				check1.addElement(now_x);
				check2.addElement(now_y);
				now_x = now_x - 1;
				now_y = now_y - 1;
				if (DPTable[now_x][now_y] == 0 || now_x == 0 || now_y == 0) {
					break;
				}
			}
		}

		Stack<Integer> temp = new Stack<Integer>();

		for (int i = 0; i < check1.size(); i++) {
			temp.push(check1.get(i) - 1);
		}

		for (int i = 0; i < check1.size(); i++) {
			check1.setElementAt(temp.pop(), i);
		}

		for (int i = 0; i < check2.size(); i++) {
			temp.push(check2.get(i) - 1);
		}
		for (int i = 0; i < check2.size(); i++) {
			check2.setElementAt(temp.pop(), i);
		}
	}

	private static void updatearray() {
		int line1 = 0;
		int line2 = 0;

		int new_char1 = 0;
		int new_char2 = 0;

		int before_line1 = 0;
		int before_line2 = 0;
		int count1 = 0;
		int count2 = 0;
		int check_count1 = 0;
		int check_count2 = 0;
		int save1 = 0;
		int save2 = 0;
		boolean change_newline = false;

		while (!(count1 > leftCharacters.size() - 1) || !(count2 > rightCharacters.size() - 1)) {
			int temp1 = 0;
			int temp2 = 0;
			for (int i = count1; i < leftCharacters.size(); i++) {
				if (leftCharacters.get(i) == '\n') {
					temp1 = i;
					break;
				}
			}

			for (int i = count2; i < rightCharacters.size(); i++) {
				if (rightCharacters.get(i) == '\n') {
					temp2 = i;
					break;
				}
			}

			if (check1.contains(temp1) && check2.contains(temp2)) {
				int check_temp1 = check1.indexOf(temp1, check_count1);
				int check_temp2 = check2.indexOf(temp2, check_count2);

				for (int i = count1; i <= temp1; i++) {
					newLeftCharacters.add(leftCharacters.get(i));
				}

				for (int i = count2; i <= temp2; i++) {
					newRightCharacters.add(rightCharacters.get(i));
				}

				if (check_temp1 - check_count1 == temp1 - before_line1
						&& check_temp2 - check_count2 == temp2 - before_line2) {
					before_line1 = temp1;
					check_count1 = check_temp1;
					before_line2 = temp2;
					check_count2 = check_temp2;
					line1 = line1 + 1;
					line2 = line2 + 1;
					count1 = temp1 + 1;
					count2 = temp2 + 1;
					continue;
				} else {
					leftHighlights.add(new Highlight(Highlight.YELLOW, line1));
					rightHighlights.add(new Highlight(Highlight.YELLOW, line2));

					for (int i = check_count1; i <= check_temp1 - 1;) {
						int now = i;
						while (check1.get(now + 1) == -1) {
							now++;
						}
						int next = now + 1;
						if (check1.get(i) + 1 == check1.get(next)) {
							i = next;
							continue;
						} else {

							int start = check1.get(i) + 1;
							int end = check1.get(next);

							start = start + new_char1;
							end = end + new_char1;
							leftHighlights.add(new Highlight(Highlight.PINK, start, end));

						}
						i = next;
					}

					for (int i = check_count2; i <= check_temp2 - 1;) {
						int now = i;
						while (check2.get(now + 1) == -1) {
							now++;
						}
						int next = now + 1;
						if (check2.get(i) + 1 == check2.get(next)) {
							i = next;
							continue;

						} else {

							int start = check2.get(i) + 1;
							int end = check2.get(next);

							start = start + new_char2;
							end = end + new_char2;
							rightHighlights.add(new Highlight(Highlight.PINK, start, end));

						}
						i = next;
					}
					before_line1 = temp1;
					check_count1 = check_temp1;
					before_line2 = temp2;
					check_count2 = check_temp2;
					line1 = line1 + 1;
					line2 = line2 + 1;
					count1 = temp1 + 1;
					count2 = temp2 + 1;
					continue;
				}
			} else {
				if (check2.contains(temp2)) {
					if (change_newline == false) {
						int dp_change1 = check_count1;

						for (int i = temp1 + 1; i < leftCharacters.size(); i++) {
							if (leftCharacters.get(i) != '\n') {
								continue;
							}

							if (check1.contains(i)) {
								dp_change1 = check1.indexOf(i, check_count1);

								save1 = i;
								break;
							}
						}

						check1.setElementAt(temp1, dp_change1);
						int gap = 0;
						for (int i = check_count1 + 1; i < dp_change1; i++) {
							if (check1.get(i) > check1.get(dp_change1)) {
								check1.setElementAt(-1, i);
								check2.setElementAt(-1, check_count2 + 1 + gap);
							}
							gap++;
						}
						change_newline = true;
						continue;
					}

					if (temp1 == save1 && change_newline == true) {
						change_newline = false;
						check1.setElementAt(save1, check_count1);
					}

					newRightCharacters.add('\n');

					rightHighlights.add(new Highlight(Highlight.GRAY, line2));

					leftHighlights.add(new Highlight(Highlight.YELLOW, line1));
					for (int i = count1; i <= temp1; i++) {
						newLeftCharacters.add(leftCharacters.get(i));
					}

					if (temp1 - before_line1 > 1) {
						int start = before_line1 + new_char1 + 1;
						int end = temp1;
						end = end + new_char1;

						leftHighlights.add(new Highlight(Highlight.PINK, start, end));
					}

					line1 = line1 + 1;
					line2 = line2 + 1;
					new_char2 = new_char2 + 1;
					before_line1 = temp1;
					count1 = temp1 + 1;
					continue;
				} else {
					if (change_newline == false) {
						int dp_change2 = check_count2;
						for (int i = temp2 + 1; i < rightCharacters.size(); i++) {

							if (rightCharacters.get(i) != '\n') {
								continue;
							}

							if (check2.contains(i)) {
								dp_change2 = check2.indexOf(i, check_count2);
								save2 = i;
								break;
							}
						}

						check2.setElementAt(temp2, dp_change2);
						int gap = 0;
						for (int i = check_count2 + 1; i < dp_change2; i++) {
							if (check2.get(i) > check2.get(dp_change2)) {
								check2.setElementAt(-1, i);
								check1.setElementAt(-1, check_count1 + 1 + gap);
							}
							gap++;
						}

						change_newline = true;
						continue;
					}

					if (temp2 == save2 && change_newline == true) {
						change_newline = false;
						check2.setElementAt(save2, check_count2);
					}

					newLeftCharacters.add('\n');

					leftHighlights.add(new Highlight(Highlight.GRAY, line1));
					rightHighlights.add(new Highlight(Highlight.YELLOW, line2));

					for (int i = count2; i <= temp2; i++) {
						newRightCharacters.add(rightCharacters.get(i));
					}

					if (temp2 - before_line2 > 1) {
						int start = before_line2 + new_char2 + 1;
						int end = temp2;
						rightHighlights.add(new Highlight(Highlight.PINK, start, end));
					}

					line1 = line1 + 1;
					line2 = line2 + 1;
					new_char1 = new_char1 + 1;
					before_line2 = temp2;
					count2 = temp2 + 1;
					continue;
				}
			}
		}
	}
}