package postman;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * 10. Listonosz
 */

public class Application {

	public static void main(String args[]) {
		int choice;
		do {
			System.out.println("   (1) Nhap du lieu tu tep");
			// System.out.println(" (2) Nhap du lieu tu dong");
			System.out.println("   (0) Thoát chương trình");
			System.out.println();
			System.out.print("Chon che do: ");
			Scanner readerFilePath = new Scanner(System.in);
			Scanner readerMin = new Scanner(System.in);
			Scanner readerMax = new Scanner(System.in);
			Scanner readerJump = new Scanner(System.in);
			Scanner readerProb = new Scanner(System.in);
			Scanner readerChoice = new Scanner(System.in);
			choice = readerChoice.nextInt();

			switch (choice) {
				case 1:
					System.out.print("Duong dan den file .txt: ");
					String filePath = readerFilePath.nextLine();
					System.out.println();

					try {
						FileCourse.run(filePath);
					} catch (NumberFormatException | IOException e) {
						System.out.println("Đường dẫn không hợp lệ");
					} catch (Exception e) {
						System.out.println("Định dạng dữ liệu không hợp lệ");
					}
					break;

				// case 2:
				// System.out.print("Nhập số đỉnh ");
				// Scanner readerNumber = new Scanner(System.in);
				// Scanner readerProbability = new Scanner(System.in);

				// try {
				// int numOfVertices = readerNumber.nextInt();
				// if (numOfVertices < 2)
				// throw new Exception();

				// System.out.print("Nhập xác suất cạnh giữa các đỉnh ( [0,1]):");
				// String probability = readerProbability.nextLine();
				// System.out.println();
				// RandomCourse.run(numOfVertices, Float.parseFloat(probability));

				// } catch (Exception e) {
				// System.out.println("Định dạng dữ liệu không chính xác!");
				// }

				// break;
				case 0:
					System.out.println("Đóng chương trình");
					readerFilePath.close();
					readerMin.close();
					readerMax.close();
					readerJump.close();
					readerProb.close();
					readerChoice.close();
					break;

				default:
					System.out.println("Done");
					break;
			}

			System.out.println();

		} while (choice != 0);

	}

	public static void showResult(Postman postman, boolean timeCounting) {
		if (timeCounting)
			Warmup.run(Constant.warmupNumOfVertices);

		Application.showResultOfMethod(postman, 2, timeCounting);
	}

	private static int showResultOfMethod(Postman postman, int algorithm, boolean timeCounting) {
		LinkedList<Integer> cycle = postman.getHeuristicCycle(Constant.beginVertice);

		System.out.print("Giai bang giai thuat heuristic ");

		if (postman.getNumOfVertices() <= Constant.shownNumOfVertices)
			showSequenceOfCycle(cycle, algorithm);

		return cycle.size();
	}

	private static void showSequenceOfCycle(LinkedList<Integer> cycle, int algorithm) {
		System.out.print("Duong di: ");
		Iterator<Integer> iter = (algorithm == 1) ? cycle.listIterator() : cycle.listIterator();
		while (iter.hasNext()) {
			System.out.print(iter.next() + (iter.hasNext() ? " > " : "  "));
		}
	}

	public static float calculateInacurrency(int exactSize, int heuristicSize) {
		if (heuristicSize <= exactSize)
			return 0;
		else {
			return (((float) (heuristicSize - exactSize) / exactSize)) * 100;
		}
	}
}