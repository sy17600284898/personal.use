package com.personal.use.collection;

import com.personal.use.model.Employee;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * splitList
 *
 * @author: shiyan
 * @version: 2019-10-11 11:41
 * @Copyrigt: 2019 www.lenovo.com Inc. All rights reserved.
 */
public class ListUtils {


    /**
     * java8 Stream 大数据量List分批处理
     *
     * @param list
     * @param maxNumber
     * @param <T>
     * @return
     */
    private static <T> List<List<T>> splitList(List<T> list, final int maxNumber) {
        return Stream.iterate(0, n -> n + 1).limit((list.size() + maxNumber - 1) / maxNumber)
                .parallel().map(a -> list.stream().skip(a * maxNumber).limit(maxNumber)
                        .parallel().collect(Collectors.toList())).collect(Collectors.toList());

    }

    /**
     * java8 Stream 分页
     *
     * @param list
     * @param pageIndex 从多少开始分页
     * @param pageSize  一页多少条
     * @param <T>
     * @return
     */
    public static <T> List<T> pageList(List<T> list, final int pageIndex, final int pageSize) {
        return list.stream().skip(pageIndex).limit(pageSize).collect(Collectors.toList());
    }


    /**
     * java8 stream 根据对象中某个属性获取集合中最大或最小的对象
     *
     * @param list
     * @param flag true max
     * @return
     */
    public static Employee maxAndMinList(List<Employee> list, final boolean flag) {
        if (flag) {
            return list.stream().max(Comparator.comparing(Employee::getId)).orElse(new Employee());
        }
        return list.stream().min(Comparator.comparing(Employee::getId)).orElse(new Employee());
    }

    /**
     * JAVA8 Stream 按照id分组
     *
     * @param list
     * @return
     */
    public static Map<Integer, List<Employee>> groupList(List<Employee> list) {
        return list.stream().collect(Collectors.groupingBy(Employee::getId));
    }

    /**
     * java8 Stream 字符串拼接
     *
     * @param empList
     */
    public static String testCollectorsJoining(List<Employee> empList) {
        return empList.stream().map(Employee::getName)
                .collect(Collectors.joining(",", "----", "----"));
    }

    /**
     * java8 Stream 接收Lambda, 从流中排除某些元素。
     *
     * @param empList
     */
    public static void testFilter(List<Employee> empList) {
        empList.stream().filter((e) -> {
            return e.getSalary() >= 5000;
        }).forEach(System.out::println);
    }

    /**
     * java8 Stream 跳过元素，返回一个扔掉了前n个元素的流。
     *
     * @param empList
     */
    public static void testSkip(List<Employee> empList) {
        empList.stream().filter((e) -> e.getSalary() >= 5000).skip(2).forEach(System.out::println);
    }

    /**
     * java8 Stream 筛选，通过流所生成元素的 hashCode() 和 equals() 去除重复元素
     *
     * @param empList
     */
    public static void testDistinct(List<Employee> empList) {
        empList.stream().distinct().forEach(System.out::println);
    }

    /**
     * java8 Stream 截断流，使其元素不超过给定数量。
     *
     * @param empList
     */
    public static void testLimit(List<Employee> empList) {
        empList.stream().filter((e) -> {
            return e.getSalary() >= 5000;
        }).limit(3).forEach(System.out::println);
    }

    /**
     * java8 Stream 接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的元素
     *
     * @param empList
     */
    public static void testMap(List<Employee> empList) {
        empList.stream().map(e -> e.getName()).forEach(System.out::println);

        empList.stream().map(e -> {
            empList.forEach(i -> {
                i.setName(i.getName() + "111");
            });
            return e;
        }).collect(Collectors.toList());
    }

    /**
     * java8 Stream 产生一个新流，其中按自然顺序排序
     *
     * @param empList
     */
    public static void testSorted(List<Employee> empList) {
        empList.stream().map(Employee::getName).sorted().forEach(System.out::println);
    }

    /**
     * java8 Stream 产生一个新流，其中按自定义排序
     *
     * @param empList
     */
    public static void testSortedComparator(List<Employee> empList) {
        empList.stream().sorted((x, y) -> {
            if (x.getAge() == y.getAge()) {
                return x.getName().compareTo(y.getName());
            } else {
                return Integer.compare(x.getAge(), y.getAge());
            }
        }).forEach(System.out::println);
    }

    /**
     * java8 Stream 检查是否至少匹配一个元素
     *
     * @param empList
     */
    public static boolean testAnyMatch(List<Employee> empList) {
        return empList.stream().anyMatch((e) -> e.getStatus().equals(Employee.Status.BUSY));
    }

    /**
     * java8 Stream 检查是否匹配所有元素
     *
     * @param empList
     */
    public static boolean testAllMatch(List<Employee> empList) {
        return empList.stream().allMatch((e) -> e.getStatus().equals(Employee.Status.BUSY));
    }

    /**
     * java8 Stream 检查是否没有匹配的元素
     *
     * @param empList
     */
    public static boolean testNoneMatch(List<Employee> empList) {
        return empList.stream().noneMatch((e) -> e.getStatus().equals(Employee.Status.BUSY));
    }

    /**
     * java8 Stream 返回第一个元素
     *
     * @param empList
     */
    public static void testFindFirst(List<Employee> empList) {
        Optional<Employee> op = empList.stream().sorted(Comparator.comparingDouble(Employee::getSalary))
                .findFirst();
        if (op.isPresent()) {
            System.out.println("first employee name is : " + op.get().getName().toString());
        }
    }

    /**
     * java8 Stream 返回当前流中的任意元素
     *
     * @param empList
     */
    public static void testFindAny(List<Employee> empList) {
        Optional<Employee> op = empList.stream().sorted(Comparator.comparingDouble(Employee::getSalary))
                .findAny();
        if (op.isPresent()) {
            System.out.println("any employee name is : " + op.get().getName().toString());
        }
    }

    /**
     * java8 Stream 返回流中元素的总个数
     *
     * @param empList
     */
    public static long testCount(List<Employee> empList) {
        return empList.stream().filter((e) -> e.getStatus().equals(Employee.Status.FREE)).count();
    }

    /**
     * java8 Stream 返回流中最大值
     *
     * @param empList
     */
    public static Optional<Double> testMax(List<Employee> empList) {
        return empList.stream().map(Employee::getSalary).max(Double::compare);
    }

    /**
     * java8 Stream 返回流中最小值
     *
     * @param empList
     */
    public static Optional<Employee> testMin(List<Employee> empList) {
        return empList.stream().min(Comparator.comparingDouble(Employee::getSalary));
    }

    /**
     * java8 Stream 可以将流中元素反复结合起来，得到一个值。返回T
     *
     * @param empList
     */
    public static Optional<Double> testReduce(List<Employee> empList) {
        return empList.stream().map(Employee::getSalary).reduce(Double::sum);
    }

    /**
     * java8 Stream 可以将流中元素反复结合起来，得到一个值，返回Optional< T>
     *
     * @param empList
     */
    public static Integer testReduce1(List<Employee> empList) {
        Optional<Integer> sum = empList.stream().map(Employee::getName).flatMap(ListUtils::filterCharacter)
                .map((ch) -> {
                    if (ch.equals('六')) {
                        return 1;
                    } else {
                        return 0;
                    }
                }).reduce(Integer::sum);
        return sum.get();
    }

    /**
     * java8 Stream 把流中的元素收集到list里。
     *
     * @param empList
     */
    public static List<String> testCollectorsToList(List<Employee> empList) {
        return empList.stream().map(Employee::getName).collect(Collectors.toList());
    }

    /**
     * java8 Stream 把流中的元素收集到set里。
     *
     * @param empList
     */
    public static Set<String> testCollectorsToSet(List<Employee> empList) {
        return empList.stream().map(Employee::getName).collect(Collectors.toSet());
    }

    /**
     * java8 Stream 把流中的元素收集到新创建的集合里。
     *
     * @param empList
     */
    public static HashSet<String> testCollectorsToCollection(List<Employee> empList) {
        return empList.stream().map(Employee::getName).collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * java8 Stream 根据比较器选择最大值。
     *
     * @param empList
     */
    public static Optional<Double> testCollectorsMaxBy(List<Employee> empList) {
        return empList.stream().map(Employee::getSalary).collect(Collectors.maxBy(Double::compare));
    }

    /**
     * java8 Stream 根据比较器选择最小值。
     *
     * @param empList
     */
    public static Optional<Double> testCollectorsMinBy(List<Employee> empList) {
        return empList.stream().map(Employee::getSalary).collect(Collectors.minBy(Double::compare));
    }

    /**
     * java8 Stream 对流中元素的整数属性求和。
     *
     * @param empList
     */
    public static Double testCollectorsSummingDouble(List<Employee> empList) {
        return empList.stream().collect(Collectors.summingDouble(Employee::getSalary));
    }

    /**
     * java8 Stream 计算流中元素Integer属性的平均值。
     *
     * @param empList
     */
    public static Double testCollectorsAveragingDouble(List<Employee> empList) {
        return empList.stream().collect(Collectors.averagingDouble(Employee::getSalary));
    }

    /**
     * java8 Stream 分组
     *
     * @param empList
     */
    public static Map<Employee.Status, List<Employee>> testCollectorsGroupingBy(List<Employee> empList) {
        return empList.stream().collect(Collectors.groupingBy(Employee::getStatus));
    }

    /**
     * java8 Stream 多级分组
     *
     * @param empList
     */
    public static Map<Employee.Status, Map<String, List<Employee>>> testCollectorsGroupingBy1(List<Employee> empList) {
        return empList.stream()
                .collect(Collectors.groupingBy(Employee::getStatus, Collectors.groupingBy((e) -> {
                    if (e.getAge() >= 60) {
                        return "老年";
                    } else if (e.getAge() >= 35) {
                        return "中年";
                    } else {
                        return "成年";
                    }
                })));
    }

    public static Stream<Character> filterCharacter(String str) {

        List<Character> list = new ArrayList<>();
        for (Character ch : str.toCharArray()) {
            list.add(ch);
        }
        return list.stream();
    }

    /**
     * java8 Stream 根据id去重
     *
     * @param list
     * @return
     */
    public static List<Employee> collectingAndThen(List<Employee> list) {
        return list.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Employee::getId))),
                ArrayList::new));
    }
}
