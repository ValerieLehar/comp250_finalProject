package finalproject.finalproject;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.lang.reflect.*;
import java.util.function.Consumer;

enum COMPETENCY { P, AM, M };

class CustomDisplayNameGenerator extends DisplayNameGenerator.Standard {
    // add if AM or M
    private static final Map<String, COMPETENCY> competencyLevels = Map.ofEntries(
            Map.entry("testMyHashTablePut2", COMPETENCY.AM),
            Map.entry("testMyHashTableGetValueSet2", COMPETENCY.AM),
            Map.entry("testMyHashIterator1", COMPETENCY.AM),
            Map.entry("testMyHashIterator2", COMPETENCY.AM),
            Map.entry("testMyHashIteratorHasNext1", COMPETENCY.AM),
            Map.entry("testMyWebGraphAddVertex2", COMPETENCY.AM),
            Map.entry("testMyWebGraphAddEdge2", COMPETENCY.AM),
            Map.entry("testMyWebGraphGetVertices2", COMPETENCY.AM),
            Map.entry("testMyWebGraphGetEdgesInto1", COMPETENCY.AM),
            Map.entry("testSortingFastSort1", COMPETENCY.AM),
            Map.entry("testSortingFastSortStress1", COMPETENCY.M),
            Map.entry("testSortingFastSortStress2", COMPETENCY.M),
            Map.entry("testSortingFastSortStress3", COMPETENCY.M),
            Map.entry("testSearchEngineCrawlAndIndexWebGraph3", COMPETENCY.AM),
            Map.entry("testSearchEngineCrawlAndIndexWebGraph4", COMPETENCY.AM),
            Map.entry("testSearchEngineComputeRanks1", COMPETENCY.AM)
    );
    // add if not 1
    private static final Map<String, Integer> competencyTokens = Map.ofEntries(

    );
    // add for minitester tests
    private static final Map<String, String> descriptions = Map.ofEntries(
            Map.entry("testMyHashTableConstructor1", "check size and capacity"),
            Map.entry("testMyHashTableConstructor2", "check buckets array"),
            Map.entry("testMyHashTablePut1", "check value for new key added and return null (check buckets)"),
            Map.entry("testMyHashTablePut2", "check value for existing key overwritten and old value returned (check buckets)"),
            Map.entry("testMyHashTableGet1", "correct return for existing key"),
            Map.entry("testMyHashTableRemove1", "correct return for existing key"),
            Map.entry("testMyHashTableRehash1", "check buckets size"),
            Map.entry("testMyHashTableRehash2", "capacity field doubled"),
            Map.entry("testMyHashTableGetKeySet1", "check all keys returned"),
            Map.entry("testMyHashTableGetValueSet1", "only unique values"),
            Map.entry("testMyHashTableGetValueSet2", "check all values returned"),
            Map.entry("testMyHashTableGetEntrySet1", "check all pairs added"),
            Map.entry("testMyHashIterator1", "test iteration 1"),
            Map.entry("testMyHashIterator2", "test iteration 2"),
            Map.entry("testMyHashIteratorHasNext1", "return true if more pairs"),
            Map.entry("testMyWebGraphAddVertex1", "add new URL; check added and return"),
            Map.entry("testMyWebGraphAddVertex2", "add existing URL; check added and return"),
            Map.entry("testMyWebGraphAddEdge1", "check edge created, check return"),
            Map.entry("testMyWebGraphAddEdge2", "from and to don't exist - check graph not modified and return"),
            Map.entry("testMyWebGraphGetVertices1", "one url"),
            Map.entry("testMyWebGraphGetVertices2", "no urls"),
            Map.entry("testMyWebGraphGetEdgesInto1", "one edge"),
            Map.entry("testSortingFastSort1", "many entries - check sorted and present"),
            Map.entry("testSortingFastSortStress1", "stress test 1"),
            Map.entry("testSortingFastSortStress2", "stress test 2"),
            Map.entry("testSortingFastSortStress3", "stress test 3"),
            Map.entry("testSearchEngineCrawlAndIndexWebGraph1", "testAcyclic: siteA - check total num vertices"),
            Map.entry("testSearchEngineCrawlAndIndexWebGraph2", "testAcyclic: siteA - check vertices are correct"),
            Map.entry("testSearchEngineCrawlAndIndexWebGraph3", "testAcyclic: siteA - check total num edges"),
            Map.entry("testSearchEngineCrawlAndIndexWebGraph4", "testAcyclic: siteA - check edges are correct"),
            Map.entry("testSearchEngineComputeRanks1", "testAcyclic - check ranks for A,C,D,E"),
            Map.entry("testSearchEngineGetResults1", "testAcyclic -- siteA,C,D"),
            Map.entry("testSearchEngineAssignPageRanks1", "e=1000; testAcyclic")
    );

    @Override
    public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
        String methodName = testMethod.getName();
        COMPETENCY competency = competencyLevels.getOrDefault(methodName, COMPETENCY.P);
        Integer tokens = competencyTokens.getOrDefault(methodName, 1);
        return competency + "(" + tokens + "): " + methodName + " - " + descriptions.get(methodName);
    }
}

@interface MiniTester { }

@DisplayName("Final Project Minitester")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayNameGeneration(CustomDisplayNameGenerator.class)
public class MiniTester_FinalProject {
	private static final double RUNTIME_FACTOR = 4.0;
    private static final int NUM_TIME_ATTEMPTS = 10;

    class MyHashTableADV<K, V> extends MyHashTable<K, V>{
        public MyHashTableADV(int c) {
            super(c);
        }

        public void setEle(MyPair<K, V> p) {
            this.getBuckets().get(hashFunction(p.getKey())).add(p);
        }
    }

    public void compareTimes(Runnable solMethod, Runnable studentMethod) {
        List<Long> solutionTimes = new ArrayList<>();
        for (int i = 0; i < NUM_TIME_ATTEMPTS; i++) {
            long startTime = System.nanoTime();
            solMethod.run();
            long endTime = System.nanoTime();
            solutionTimes.add(endTime - startTime);
        }

        List<Long> studentTimes = new ArrayList<>();
        for (int i = 0; i < NUM_TIME_ATTEMPTS; i++) {
            long startTime = System.nanoTime();
            studentMethod.run();
            long endTime = System.nanoTime();
            studentTimes.add(endTime - startTime);
        }

        long averageSolutionTime = solutionTimes.stream().mapToLong(Long::longValue).sum() / NUM_TIME_ATTEMPTS;
        long averageStudentTime = studentTimes.stream().mapToLong(Long::longValue).sum() / NUM_TIME_ATTEMPTS;

        assertTrue(averageStudentTime <= averageSolutionTime * RUNTIME_FACTOR,
                "Student method took too long. Expected: less than" + averageSolutionTime * RUNTIME_FACTOR + ", Actual: " + averageStudentTime);
    }

    //
    // MyHashTable tests
    //

    @Test
    @MiniTester
    public void testMyHashTableConstructor1() {
        MyHashTable<String, Integer> h = new MyHashTable<>();

        assertEquals(0, h.size());
        assertEquals(16, h.numBuckets());
    }

    @Test
    @MiniTester
    public void testMyHashTableConstructor2() {
        MyHashTable<String, Integer> h = new MyHashTable<>();

        ArrayList<LinkedList<MyPair<String, Integer>>> L = h.getBuckets();
        assertEquals(16, L.size());
        for (int i = 0; i < L.size(); i++) {
            assertEquals(0, L.get(i).size());
        }
    }

    @Test
    @MiniTester
    public void testMyHashTablePut1() {
        MyHashTable<String, Integer> h = new MyHashTable<>(2);

        assertNull(h.put("Hello", 1337));

        ArrayList<LinkedList<MyPair<String, Integer>>> L = h.getBuckets();

        assertEquals(1, L.get(0).size());
        assertEquals(0, L.get(1).size());
        assertEquals("Hello", L.get(0).get(0).getKey());
    }

    @Test
    @MiniTester
    public void testMyHashTablePut2() {
        MyHashTable<String, Integer> h = new MyHashTable<>(2);

        assertNull(h.put("Hello", 1337));
        assertEquals(1337, h.put("Hello", 42));

        ArrayList<LinkedList<MyPair<String, Integer>>> L = h.getBuckets();
        assertEquals(1, L.get(0).size());
        assertEquals(0, L.get(1).size());
        assertEquals("Hello", L.get(0).get(0).getKey());
        assertEquals(42, L.get(0).get(0).getValue());
    }

    @Test
    @MiniTester
    public void testMyHashTableGet1() {
        MyHashTable<String, Integer> h = new MyHashTable<>();

        h.put("Hello", 1337);
        h.put("World", 42);

        assertEquals(1337, h.get("Hello"));
        assertEquals(42, h.get("World"));
    }

    @Test
    @MiniTester
    public void testMyHashTableRemove1() {
        MyHashTable<String, Integer> h = new MyHashTable<>();
        h.put("Hello", 1337);
        assertEquals(1337, h.remove("Hello"));
    }

    @Test
    @MiniTester
    public void testMyHashTableRehash1() {
        MyHashTableADV<String, Integer> h = new MyHashTableADV<>(6);
        h.setEle(new MyPair<String, Integer>("Hello", 1337));
        h.setEle(new MyPair<String, Integer>("World1", 42));
        h.setEle(new MyPair<String, Integer>("Goodbye", 1338));
        h.setEle(new MyPair<String, Integer>("World2", 1339));

        h.rehash();

        ArrayList<LinkedList<MyPair<String, Integer>>> L = h.getBuckets();
        assertEquals(1, L.get(2).size());
        assertEquals(1, L.get(4).size());
        assertEquals(2, L.get(5).size());
    }

    @Test
    @MiniTester
    public void testMyHashTableRehash2() {
        MyHashTableADV<Integer, String> h = new MyHashTableADV<>(12);
        h.setEle(new MyPair<Integer, String>(1, "A"));
        h.setEle(new MyPair<Integer, String>(2, "B"));
        h.setEle(new MyPair<Integer, String>(3, "C"));
        h.setEle(new MyPair<Integer, String>(4, "D"));
        h.setEle(new MyPair<Integer, String>(13, "E"));
        h.setEle(new MyPair<Integer, String>(14, "F"));
        h.setEle(new MyPair<Integer, String>(15, "G"));
        h.setEle(new MyPair<Integer, String>(16, "H"));

        h.rehash();

        ArrayList<LinkedList<MyPair<Integer, String>>> L = h.getBuckets();
        assertEquals(24, L.size());
    }

    @Test
    @MiniTester
    public void testMyHashTableGetKeySet1() {
        MyHashTableADV<Integer, String> h = new MyHashTableADV<Integer, String>(12);

        h.setEle(new MyPair<Integer, String>(1, "A"));
        h.setEle(new MyPair<Integer, String>(2, "B"));
        h.setEle(new MyPair<Integer, String>(3, "C"));
        h.setEle(new MyPair<Integer, String>(4, "D"));

        h.setEle(new MyPair<Integer, String>(13, "E"));
        h.setEle(new MyPair<Integer, String>(14, "F"));
        h.setEle(new MyPair<Integer, String>(15, "G"));
        h.setEle(new MyPair<Integer, String>(16, "H"));

        List<Integer> expected = List.of(1, 2, 3, 4, 13, 14, 15, 16);
        ArrayList<Integer> received = h.keySet();
        received.sort(Comparator.naturalOrder());

        assertEquals(expected, received);
    }

    @Test
    @MiniTester
    public void testMyHashTableGetValueSet1() {
        MyHashTableADV<Integer, String> h = new MyHashTableADV<Integer, String>(10);

        h.setEle(new MyPair<Integer, String>(3, "Hello"));
        h.setEle(new MyPair<Integer, String>(9, "World"));
        h.setEle(new MyPair<Integer, String>(15, "Goodbye"));
        h.setEle(new MyPair<Integer, String>(21, "World"));

        List<String> expected = List.of("Goodbye", "Hello", "World");
        ArrayList<String> received = h.values();
        received.sort(Comparator.naturalOrder());

        assertEquals(expected, received);
    }

    @Test
    @MiniTester
    public void testMyHashTableGetValueSet2() {
        MyHashTableADV<Integer, Character> h = new MyHashTableADV<Integer, Character>(12);

        h.setEle(new MyPair<Integer, Character>(1, 'A'));
        h.setEle(new MyPair<Integer, Character>(2, 'B'));
        h.setEle(new MyPair<Integer, Character>(3, 'C'));
        h.setEle(new MyPair<Integer, Character>(4, 'D'));

        h.setEle(new MyPair<Integer, Character>(13, 'E'));
        h.setEle(new MyPair<Integer, Character>(14, 'F'));
        h.setEle(new MyPair<Integer, Character>(15, 'G'));
        h.setEle(new MyPair<Integer, Character>(16, 'H'));

        ArrayList<Character> received = h.values();
        received.sort(Comparator.naturalOrder());
        for (int i = 0; i < 8; i++)
            assertEquals((char) ('A' + i), received.get(i));
    }

    @Test
    @MiniTester
    public void testMyHashTableGetEntrySet1() {
        MyHashTableADV<Integer, String> h = new MyHashTableADV<Integer, String>(10);
        List<MyPair<Integer, String>> expected = List.of(new MyPair<Integer, String>(3, "Hello"),
                new MyPair<Integer, String>(9, "World"),
                new MyPair<Integer, String>(15, "Goodbye"), new MyPair<Integer, String>(21, "World"));
        for (MyPair<Integer, String> p : expected)
            h.setEle(p);

        ArrayList<MyPair<Integer, String>> received = h.entrySet();
        received.sort(Comparator.comparing(MyPair<Integer, String>::getKey));

        assertEquals(expected, received);
    }

    //
    // MyHashIterator tests
    //

    @Test
    @MiniTester
    public void testMyHashIterator1() {
        MyHashTableADV<Integer, String> h = new MyHashTableADV<Integer, String>(10);
        ArrayList<MyPair<Integer, String>> expected = new ArrayList<>(
                List.of(new MyPair<Integer, String>(3, "Hello"), new MyPair<Integer, String>(9, "World"),
                        new MyPair<Integer, String>(15, "Goodbye"), new MyPair<Integer, String>(21, "World")));
        for (MyPair<Integer, String> p : expected)
            h.setEle(p);

        for (MyPair<Integer, String> p : h) {
            assertTrue(expected.remove(p));
        }
        assertEquals(0, expected.size());
    }

    @Test
    @MiniTester
    public void testMyHashIterator2() {
        MyHashTableADV<Integer, String> h = new MyHashTableADV<Integer, String>(12);

        ArrayList<MyPair<Integer, String>> expected = new ArrayList<>(
                List.of(new MyPair<Integer, String>(1, "A"), new MyPair<Integer, String>(2, "B"),
                        new MyPair<Integer, String>(3, "C"), new MyPair<Integer, String>(4, "D"),
                        new MyPair<Integer, String>(13, "E"),
                        new MyPair<Integer, String>(14, "F"), new MyPair<Integer, String>(15, "G"),
                        new MyPair<Integer, String>(16, "H")));

        for (MyPair<Integer, String> p : expected)
            h.setEle(p);

        for (MyPair<Integer, String> p : h) {
            assertTrue(expected.remove(p));
        }
        assertEquals(0, expected.size());
    }

    @Test
    @MiniTester
    public void testMyHashIteratorHasNext1() {
        MyHashTableADV<Integer, String> h = new MyHashTableADV<Integer, String>(10);
        ArrayList<MyPair<Integer, String>> expected = new ArrayList<>(
                List.of(new MyPair<Integer, String>(3, "Hello"), new MyPair<Integer, String>(9, "World"),
                        new MyPair<Integer, String>(15, "Goodbye"), new MyPair<Integer, String>(21, "World")));
        for (MyPair<Integer, String> p : expected)
            h.setEle(p);

        Iterator<MyPair<Integer, String>> it = h.iterator();
        assertTrue(it.hasNext());
    }

    //
    // MyWebGraph tests
    //

	@Test
    @MiniTester
	void testMyWebGraphAddVertex1() {
		MyWebGraph web = new MyWebGraph();
		boolean[] result = new boolean[1];

        result[0] = web.addVertex("url_1");

		MyWebGraph.WebVertex v = web.vertexList.get("url_1");
		assertTrue(v!=null, "No vertex has been added to the graph with the given url");
		assertTrue(v.toString().contains("url_1"), "The vertex added does not have the correct url");
		assertEquals(0, v.getNeighbors().size(), "Unwanted edges have been added to the graph");
		assertTrue(web.vertexList.size()<=1, "There's more than one vertex in the graph");
		assertTrue(result[0], "The method did not return true");
	}

    @Test
    @MiniTester
    void testMyWebGraphAddVertex2() {
        MyWebGraph web = new MyWebGraph();
        boolean[] result = new boolean[1];

        web.addVertex("url_1");
        web.setPageRank("url_1", 5);
        result[0] = web.addVertex("url_1");

        assertEquals(1, web.vertexList.size(), "The size of the graph has changed");
        assertTrue(!result[0], "The method returned true");
        assertEquals(5, web.getPageRank("url_1"), "The method updated the existing vertex");
    }

	@Test
    @MiniTester
	void testMyWebGraphAddEdge1() {
		MyWebGraph web = new MyWebGraph();
		boolean[] result = new boolean[1];

		MyWebGraph.WebVertex v1 = web.new WebVertex("url_1");
		MyWebGraph.WebVertex v2 = web.new WebVertex("url_2");
		web.vertexList.put("url_1", v1);
		web.vertexList.put("url_2", v2);

        result[0] = web.addEdge("url_1", "url_2");

		assertEquals(2, web.vertexList.size(), "The number of vertices changed");
		assertTrue(v1.containsEdge("url_2"), "The edge has not been added correctly");
		assertEquals(0, v2.getNeighbors().size(), "An extra edge has been added to the graph.");
		assertTrue(result[0], "The method did not return true.");
	}

    @Test
    @MiniTester
    void testMyWebGraphAddEdge2() {
        MyWebGraph web = new MyWebGraph();
        boolean[] result = new boolean[1];

        MyWebGraph.WebVertex v1 = web.new WebVertex("url_1");
        MyWebGraph.WebVertex v2 = web.new WebVertex("url_2");
        web.vertexList.put("url_1", v1);
        web.vertexList.put("url_2", v2);

        result[0] = web.addEdge("url_3", "url_4");

        assertEquals(2, web.vertexList.size(), "The number of vertices changed");
        assertEquals(0, v1.getNeighbors().size(), "An unwanted edge has been added to the graph.");
        assertEquals(0, v2.getNeighbors().size(), "An unwanted edge has been added to the graph.");
        assertTrue(!result[0], "The method did not return false.");
    }

	@Test
    @MiniTester
	void testMyWebGraphGetVertices1() {
		MyWebGraph web = new MyWebGraph();
		ArrayList<ArrayList<String>> vertices = new ArrayList<>();
		MyWebGraph.WebVertex v1 = web.new WebVertex("url_1");
		web.vertexList.put("url_1", v1);

        vertices.add(web.getVertices());

		if (vertices.size()==0 || vertices.get(0)==null)
			fail("The method did not execute correctly or was not implemented");
		ArrayList<String> urls = vertices.get(0);
		assertEquals(1, urls.size(), "The number of urls returned is incorrect");
		assertTrue(urls.contains("url_1"), "The url returned are not correct");
		assertEquals(1, web.vertexList.size(), "The graph has been modified");
	}

	@Test
    @MiniTester
	void testMyWebGraphGetVertices2() {
		MyWebGraph web = new MyWebGraph();
		ArrayList<ArrayList<String>> vertices = new ArrayList<>();

        vertices.add(web.getVertices());

		if (vertices.size()==0 || vertices.get(0)==null)
			fail("The method did not execute correctly or was not implemented");
		ArrayList<String> urls = vertices.get(0);
		assertEquals(0, urls.size(), "A non-empty list has been returned");
	}

    @Test
    @MiniTester
    void testMyWebGraphGetEdgesInto1() {
        MyWebGraph web = new MyWebGraph();

        MyWebGraph.WebVertex v1 = web.new WebVertex("url_1");
        MyWebGraph.WebVertex v2 = web.new WebVertex("url_2");
        MyWebGraph.WebVertex v3 = web.new WebVertex("url_3");
        MyWebGraph.WebVertex v4 = web.new WebVertex("url_4");
        web.vertexList.put("url_1", v1);
        web.vertexList.put("url_2", v2);
        web.vertexList.put("url_3", v3);
        web.vertexList.put("url_4", v4);

        // v1 in-degree 0, out-degree 2
        // v2 in-degree 2, out-degree 1
        // v3 in-degree 1, out-degree 0
        // v4 in-degree 2, out-degree 2
        v1.addEdge("url_2");

        ArrayList<ArrayList<String>> results = new ArrayList<>();
        results.add(web.getEdgesInto("url_2"));

        assertEquals(1, web.getOutDegree("url_1"), "The adj list of url_1 has been modified.");
        assertEquals(1, results.get(0).size(), "The arraylist returned is not empty.");
    }

    //
    // Sorting tests
    //

    //private static boolean fastSortCorrectTest1 = false;

    @Test
    @MiniTester
    void testSortingFastSort1() {
        MyHashTableADV<Integer, Integer> toSort = new MyHashTableADV<>(100);

        Random rand = new Random(1234);
        ArrayList<Integer> randomNumbers = new ArrayList<>();
        for (int j = 0; j < 100; j++) {
            int randNum = rand.nextInt(1000);
            while (randomNumbers.contains(randNum)) {
                randNum = rand.nextInt(1000);
            }
            randomNumbers.add(randNum);
            toSort.setEle(new MyPair<>(randNum, randNum));
        }

        ArrayList<Integer> results = Sorting.fastSort(toSort);

        assertEquals(100, results.size());

        Collections.sort(randomNumbers, Collections.reverseOrder());
        assertEquals(randomNumbers, results);
        //fastSortCorrectTest1 = true;
    }

    public void solutionFastSortStress(int mapSize) {
        MyHashTable<Integer, Integer> map = new MyHashTable<>();
        for (int j = 0; j <= mapSize; j++) {
            int val = j + (int)(Math.random() * (mapSize-j+1));
            map.put(val, val);
        }
        Collections.sort(new ArrayList<Integer>(map.values()));
    }

    public void studentFastSortStress(int mapSize) {
        MyHashTable<Integer, Integer> map = new MyHashTable<>();
        for (int j = 0; j <= mapSize; j++) {
            int val = j + (int)(Math.random() * (mapSize-j+1));
            map.put(val, val);
        }
        Sorting.fastSort(map);
    }

    @Test
    @MiniTester
    void testSortingFastSortStress1() {
        int mapSize = 1000;
        compareTimes(
                () -> { solutionFastSortStress(mapSize); },
                () -> { studentFastSortStress(mapSize); }
        );
    }

    @Test
    @MiniTester
    void testSortingFastSortStress2() {
        int mapSize = 10000;
        compareTimes(
                () -> { solutionFastSortStress(mapSize); },
                () -> { studentFastSortStress(mapSize); }
        );
    }

    @Test
    @MiniTester
    void testSortingFastSortStress3() {
        int mapSize = 100000;
        compareTimes(
                () -> { solutionFastSortStress(mapSize); },
                () -> { studentFastSortStress(mapSize); }
        );
    }

    //
    // SearchEngine tests
    //

    public void tryWithSearchEngines(String filename, Consumer<SearchEngine> consumer) throws Exception {
        SearchEngine searchEngine = new SearchEngine(filename);
        consumer.accept(searchEngine);
    }

    @Test
    @MiniTester
    void testSearchEngineCrawlAndIndexWebGraph1() throws Exception {
        tryWithSearchEngines("testAcyclic.xml", searchEngine -> {
            searchEngine.crawlAndIndex("siteA");
            assertEquals(4, searchEngine.internet.vertexList.size(), "The number of vertices added while crawling is incorrect.");
        });
    }

    @Test
    @MiniTester
    void testSearchEngineCrawlAndIndexWebGraph2() throws Exception {
        String[] expectedUrls = {"siteA", "siteC", "siteD", "siteE"};
        tryWithSearchEngines("testAcyclic.xml", searchEngine -> {
            searchEngine.crawlAndIndex("siteA");

            for (String s : expectedUrls) {
                if (!searchEngine.internet.vertexList.keySet().contains(s))
                    fail("Expected to find " + s + " in the graph, but it is not there");
            }
        });
    }

    @Test
    @MiniTester
    void testSearchEngineCrawlAndIndexWebGraph3() throws Exception {
        tryWithSearchEngines("testAcyclic.xml", searchEngine -> {
            searchEngine.crawlAndIndex("siteA");

            int numEdges = 0;
            for (String s : searchEngine.internet.vertexList.keySet()) {
                numEdges += searchEngine.internet.getOutDegree(s);
            }
            assertTrue(numEdges == 4, "The number of edges added while crawling is incorrect");
        });
    }

    @Test
    @MiniTester
    void testSearchEngineCrawlAndIndexWebGraph4() throws Exception {
        tryWithSearchEngines("testAcyclic.xml", searchEngine -> {
            MyHashTable<String, String[]> expectedEdges = new MyHashTable<>();
            expectedEdges.put("siteE", new String[]{});
            expectedEdges.put("siteC", new String[]{"siteE"});
            expectedEdges.put("siteD", new String[]{"siteE"});
            expectedEdges.put("siteA", new String[]{"siteC", "siteD"});

            searchEngine.crawlAndIndex("siteA");

            assertEquals(expectedEdges.keySet(), searchEngine.internet.vertexList.keySet(),
                    "The graph does not have the correct vertices");
            for (String url : searchEngine.internet.vertexList.keySet()) {
                String[] neighbors = expectedEdges.get(url);
                assertEquals(neighbors.length, searchEngine.internet.getOutDegree(url), "Incorrect out-degree");
                for (String n : neighbors)
                    assertTrue(searchEngine.internet.vertexList.get(url).containsEdge(n),
                            n + " is not a part of the adj list of " + url);
            }
        });
    }

    @Test
    @MiniTester
    void testSearchEngineComputeRanks1() throws Exception {
        MyHashTable<String, Double> expectedResults = new MyHashTable<>();
        expectedResults.put("siteA", 0.5);
        expectedResults.put("siteC", 0.75);
        expectedResults.put("siteD", 0.75);
        expectedResults.put("siteE", 1.5);

        tryWithSearchEngines("testAcyclic.xml", searchEngine -> {
            MyWebGraph.WebVertex A = searchEngine.internet.new WebVertex("siteA");
            MyWebGraph.WebVertex C = searchEngine.internet.new WebVertex("siteC");
            MyWebGraph.WebVertex D = searchEngine.internet.new WebVertex("siteD");
            MyWebGraph.WebVertex E = searchEngine.internet.new WebVertex("siteE");
            C.addEdge("siteE");
            D.addEdge("siteE");
            A.addEdge("siteC");
            A.addEdge("siteD");

            searchEngine.internet.vertexList.put("siteA", A);
            searchEngine.internet.vertexList.put("siteC", C);
            searchEngine.internet.vertexList.put("siteD", D);
            searchEngine.internet.vertexList.put("siteE", E);

            ArrayList<String> vertices = new ArrayList<>();
            vertices.add("siteA");
            vertices.add("siteC");
            vertices.add("siteD");
            vertices.add("siteE");

            // set initial values for the ranks
            for (String v : vertices)
                searchEngine.internet.setPageRank(v, 1.0);

            ArrayList<ArrayList<Double>> rankAfterOneIteration = new ArrayList<>();

            rankAfterOneIteration.add(searchEngine.computeRanks(vertices));

            // verify results
            if (rankAfterOneIteration.size() != 1) {
                fail("ComputeRanks did not properly execute.");
            }

            ArrayList<Double> studentComputedRanks = rankAfterOneIteration.get(0);
            if (studentComputedRanks == null || studentComputedRanks.size() != vertices.size()) {
                fail("The arraylist returned is not of the correct size");
            }

            for (int i = 0; i < vertices.size(); i++) {
                String vertex = vertices.get(i);
                Double rank = studentComputedRanks.get(i);
                Double expectedRank = expectedResults.get(vertex);

                assertTrue(Math.abs(expectedRank - rank) <= 1e-5);
            }
        });
    }

    @Test
    @MiniTester
    void testSearchEngineGetResults1() throws Exception {
        tryWithSearchEngines("testAcyclic.xml", searchEngine -> {
            MyWebGraph.WebVertex A = searchEngine.internet.new WebVertex("siteA");
            MyWebGraph.WebVertex C = searchEngine.internet.new WebVertex("siteC");
            MyWebGraph.WebVertex D = searchEngine.internet.new WebVertex("siteD");
            MyWebGraph.WebVertex E = searchEngine.internet.new WebVertex("siteE");

            searchEngine.internet.vertexList.put("siteA", A);
            searchEngine.internet.vertexList.put("siteC", C);
            searchEngine.internet.vertexList.put("siteD", D);
            searchEngine.internet.vertexList.put("siteE", E);

            searchEngine.internet.setPageRank("siteA", 1.5);
            searchEngine.internet.setPageRank("siteC", 0.5);
            searchEngine.internet.setPageRank("siteD", 1);
            searchEngine.internet.setPageRank("siteE", 1.2);

            ArrayList<String> urls = new ArrayList<>();
            urls.add("siteA");
            urls.add("siteC");
            urls.add("siteD");

            searchEngine.wordIndex.put("test", urls);

            ArrayList<String> expectedResult = new ArrayList<>();
            expectedResult.add("siteA");
            expectedResult.add("siteD");
            expectedResult.add("siteC");

            // call method to be tested
            ArrayList<String> rankedUrls = searchEngine.getResults("test");

            // verify results
            assertEquals(3, rankedUrls.size(), "The method does not return the correct number of elements");
            assertEquals(expectedResult, rankedUrls, "The method does not return the urls in the correct order");
        });
    }

    @Test
    @MiniTester
    void testSearchEngineAssignPageRanks1() throws Exception {
        tryWithSearchEngines("testAcyclic.xml", searchEngine -> {
            MyWebGraph.WebVertex A = searchEngine.internet.new WebVertex("siteA");
            MyWebGraph.WebVertex C = searchEngine.internet.new WebVertex("siteC");
            MyWebGraph.WebVertex D = searchEngine.internet.new WebVertex("siteD");
            MyWebGraph.WebVertex E = searchEngine.internet.new WebVertex("siteE");
            C.addEdge("siteE");
            D.addEdge("siteE");
            A.addEdge("siteC");
            A.addEdge("siteD");

            // store them in the graph
            searchEngine.internet.vertexList.put("siteA", A);
            searchEngine.internet.vertexList.put("siteC", C);
            searchEngine.internet.vertexList.put("siteD", D);
            searchEngine.internet.vertexList.put("siteE", E);

            ArrayList<String> vertices = new ArrayList<>();
            vertices.add("siteA");
            vertices.add("siteC");
            vertices.add("siteD");
            vertices.add("siteE");

            searchEngine.assignPageRanks(1000);

            // verify results
            for (String v : vertices) {
                Double pr = searchEngine.internet.getPageRank(v);
                if (Double.isInfinite(pr))
                    fail("Page rank for vertex " + v + " is infinite. Check for divide by zero errors.");

                if (Double.isNaN(pr))
                    fail("Page rank for vertex " + v + " is NaN. Check for divide by zero errors.");

                if (pr <= 0) {
                    fail("Page rank for vertex " + v + " is negative. Page rank should be positive at each iteration.");
                }
            }
        });
    }
}