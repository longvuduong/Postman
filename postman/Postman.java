package postman;

import java.util.*;

/**
 * 10. Listonosz
 */

public class Postman {

    private Graph graph;
    private Graph hGraph;
    // Lưu lại các đỉnh đã đi qua
    private Set<Integer> visitedVertices;

    Postman(int n) {

        graph = new Graph(n);
        hGraph = new Graph(n);
        visitedVertices = new HashSet<>();
    }

    Postman(List<Integer> list) {
        this(list.get(0));
        makeGraphFromFile(list);
    }

    private void makeGraphFromFile(List<Integer> list) {
        for (int i = 1; i < list.size() - 1; i += 2)
            addEdge(list.get(i), list.get(i + 1));
    }

    public void createGraph(boolean adjacencyMatrix[][]) {
        for (int i = 0; i < graph.getNumOfVertices(); ++i) {
            for (int j = i + 1; j < graph.getNumOfVertices(); ++j) {
                if (adjacencyMatrix[i][j])
                    addEdge(i, j);
            }
        }
    }

    private void addEdge(int v, int u) {
        graph.addEdge(v, u);
        hGraph.addEdge(v, u);
    }

    public int getNumOfVertices() {
        return graph.getNumOfVertices();
    }

    public int getNumOfEdges() {
        return graph.getNumOfEdges();
    }

    public boolean isConnected() {

        if (graph.getNumOfVertices() > 0) {

            clearVisitedVertices();
            DFS(0);
            return graph.getNumOfVertices() == visitedVertices.size();
        }
        return false;
    }

    private void clearVisitedVertices() {

        if (!visitedVertices.isEmpty())
            visitedVertices.clear();
    }

    private void DFS(int v) {

        visitedVertices.add(v);

        for (Integer u : graph.getAdjacencyList()[v]) {
            if (!visitedVertices.contains(u))
                DFS(u);
        }
    }

    // Gải bằng thuật toán Heuristic
    public LinkedList<Integer> getHeuristicCycle(int beginVertice) {
        // cycleList: Danh sách liên kết chứa các đỉnh của chu trình.
        // previousVertices: Mảng chứa đỉnh trước đó của mỗi đỉnh trong chu trình.
        // startVertices: Danh sách chứa các đỉnh bắt đầu của các chu trình.
        // start: Đỉnh bắt đầu của chu trình.
        // current: Đỉnh hiện tại đang được xét.
        // tmp: Biến tạm thời để lưu đỉnh kế tiếp trong quá trình tạo chu trình.
        // index: Chỉ số hiện tại trong chu trình.
        // flag: Cờ để kiểm tra xem đỉnh hiện tại có được thêm vào chu trình hay không.
        LinkedList<Integer> cycleList = new LinkedList<>();
        Integer[] previousVertices = new Integer[hGraph.getNumOfVertices()];
        ArrayList<Integer> startVertices = new ArrayList<>();
        Integer start = beginVertice;
        Integer current = beginVertice;
        Integer tmp;
        int index = 0;
        boolean flag = true;

        visitedVertices.clear();
        visitedVertices.add(current);
        startVertices.add(start);

        // int count = 0;
        while (true) {
            // count++;
            if (flag)
                cycleList.add(index, current);

            index++;
            flag = true;
            tmp = checkNeighbours(current);

            if (tmp != null) {

                if (previousVertices[tmp] == null || current.equals(start))
                    previousVertices[tmp] = current;

                visitedVertices.add(tmp);
                hGraph.getAdjacencyList()[current].remove(tmp);
                hGraph.getAdjacencyList()[tmp].remove(current);
                current = tmp;

            } else {

                assert current != null;
                if (current.equals(start)) {

                    if (!hasUnvisitedEdges())
                        break;

                    else {
                        current = getVerticeWithUnvisitedEdge(cycleList);
                        start = current;
                        startVertices.add(start);
                        index = cycleList.indexOf(current);
                        flag = false;
                    }

                } else {

                    if (!checkLoop(start, current, previousVertices))
                        current = previousVertices[current];

                    else {
                        LinkedList<Integer> loop = getLoop(current, previousVertices);
                        LinkedList<Integer> part = leaveLoop(cycleList, loop, startVertices);
                        cycleList.addAll(index, part);
                        current = start;
                    }
                }
            }
        }

        // System.out.println("count" + count);
        return cycleList;
    }

    // Kiểm tra các đỉnh kề với đỉnh đang xét(cũng có thể gọi là hàng xóm)
    private Integer checkNeighbours(Integer v) {
        if (v == null)
            return null;

        LinkedList<Integer> list = hGraph.getAdjacencyList()[v];
        if (!list.isEmpty()) {
            Iterator<Integer> it = list.listIterator();
            Integer value = it.next();
            while (visitedVertices.contains(value) && it.hasNext())
                value = it.next();
            return value;
        } else
            return null;
    }

    // hàm này sẽ kiểu tra các cạnh đã được đi qua hết hay chưa
    private boolean hasUnvisitedEdges() {
        for (int i = 0; i < hGraph.getNumOfVertices(); ++i) {
            if (!hGraph.getAdjacencyList()[i].isEmpty())
                return true;
        }
        return false;
    }

    // Hàm này sẽ làm nhiệm vụ lấy ra các đỉnh và cạnh chưa đi qua
    private Integer getVerticeWithUnvisitedEdge(LinkedList<Integer> list) {
        Iterator<Integer> it = list.listIterator();
        Integer value;
        while (it.hasNext()) {
            value = it.next();
            if (!hGraph.getAdjacencyList()[value].isEmpty()) {
                return value;
            }
        }
        return null;
    }

    private boolean checkLoop(Integer start, Integer current, Integer[] prev) {
        Integer pom = current;
        for (int i = 0; i < getNumOfVertices(); ++i) {
            pom = prev[pom];
            if (pom == null || pom.equals(start))
                return false;
            else if (pom.equals(current))
                return true;
        }
        return false;
    }

    private LinkedList<Integer> getLoop(Integer current, Integer[] prev) {
        LinkedList<Integer> list = new LinkedList<>();
        Integer pom = current;
        while (true) {
            list.add(pom);
            pom = prev[pom];
            if (pom.equals(current))
                break;
        }
        return list;
    }

    private LinkedList<Integer> leaveLoop(LinkedList<Integer> cycleList, LinkedList<Integer> loop,
            ArrayList<Integer> startVertices) {
        LinkedList<Integer> partList = new LinkedList<>();
        int posInLoop = 0;
        int posInCycle = 0;
        int posOfStartVertice = cycleList.indexOf(startVertices.get(startVertices.size() - 1));
        boolean flag = false;

        for (int i = 0; i < loop.size(); ++i) {
            if (startVertices.contains(loop.get(i))) {
                if (i > loop.size() / 2)
                    flag = true;

                posInLoop = i;
                break;
            }
        }

        for (int i = posOfStartVertice; i >= 0; --i) {
            if (cycleList.get(i).equals(loop.get(posInLoop))) {
                posInCycle = i;
                break;
            }
        }

        if (!flag) {
            for (int i = 1; i <= posInLoop; ++i)
                partList.add(loop.get(i));
        } else {
            for (int i = loop.size() - 1; i >= posInLoop; --i)
                partList.add(loop.get(i));
        }

        for (int i = posInCycle; i < posOfStartVertice; ++i)
            partList.add(cycleList.get(i));

        return partList;
    }
}
