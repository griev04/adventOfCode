package day1

fun main() {
    println("Hello world")
    expenses = rawData.split("\n").map{ it.toInt() }
    // Part 1
    println("Part 1")
    println(processCouple(2020))
    println("Part 2")
    println(processTriple(2020))
    println(processTripleNested(2020))
}

fun processCouple(total: Int): Int? {
    val expenseComplements = mutableMapOf<Int, Int>()
    for (expense in expenses) {
        val complement = total - expense
        if (expenseComplements[complement] != null) {
            return expense * complement
        }
        expenseComplements[expense] = complement
    }
    return null
}

fun processTriple(total: Int): Int? {
    expenses.forEach{ expense ->
        val difference = total - expense
        val nested = processCouple(difference)
        if (nested != null) {
            return expense * nested
        }
    }
    return null
}

fun processTripleNested(total: Int): Int? {
    expenses.forEach{ expense ->
        val difference = total - expense
        if (difference == 0) {
            return expense
        }
        if (difference > 0) {
            val nested = processTripleNested(difference)
            if (nested != null) {
                return expense * nested
            }
        }
    }
    return null
}

var expenses = listOf<Int>()

val rawData = "1254\n" +
        "1313\n" +
        "1456\n" +
        "1782\n" +
        "1742\n" +
        "1391\n" +
        "1273\n" +
        "1286\n" +
        "1373\n" +
        "1891\n" +
        "1188\n" +
        "1994\n" +
        "1887\n" +
        "1816\n" +
        "1984\n" +
        "961\n" +
        "1428\n" +
        "1105\n" +
        "1123\n" +
        "1699\n" +
        "1154\n" +
        "1953\n" +
        "1977\n" +
        "1450\n" +
        "1696\n" +
        "1068\n" +
        "1241\n" +
        "1926\n" +
        "1228\n" +
        "1591\n" +
        "1789\n" +
        "1966\n" +
        "1508\n" +
        "1193\n" +
        "1190\n" +
        "1654\n" +
        "444\n" +
        "1282\n" +
        "1169\n" +
        "1165\n" +
        "1778\n" +
        "1669\n" +
        "1570\n" +
        "1671\n" +
        "1845\n" +
        "1208\n" +
        "1728\n" +
        "1798\n" +
        "847\n" +
        "1300\n" +
        "1817\n" +
        "1200\n" +
        "1297\n" +
        "1658\n" +
        "1296\n" +
        "1571\n" +
        "1991\n" +
        "1305\n" +
        "1314\n" +
        "1903\n" +
        "1472\n" +
        "1359\n" +
        "1506\n" +
        "1999\n" +
        "1877\n" +
        "1168\n" +
        "1137\n" +
        "1288\n" +
        "1083\n" +
        "1656\n" +
        "413\n" +
        "1430\n" +
        "1408\n" +
        "1397\n" +
        "1846\n" +
        "1218\n" +
        "684\n" +
        "1234\n" +
        "2007\n" +
        "900\n" +
        "1604\n" +
        "1460\n" +
        "1848\n" +
        "1693\n" +
        "1324\n" +
        "1401\n" +
        "1968\n" +
        "1918\n" +
        "1975\n" +
        "1665\n" +
        "1413\n" +
        "1874\n" +
        "1852\n" +
        "1521\n" +
        "1753\n" +
        "1229\n" +
        "1940\n" +
        "1650\n" +
        "1976\n" +
        "1235\n" +
        "1130\n" +
        "1927\n" +
        "1365\n" +
        "1908\n" +
        "1441\n" +
        "1302\n" +
        "1986\n" +
        "1449\n" +
        "1692\n" +
        "1944\n" +
        "1277\n" +
        "1312\n" +
        "1826\n" +
        "1231\n" +
        "1289\n" +
        "1814\n" +
        "1170\n" +
        "1606\n" +
        "1608\n" +
        "1773\n" +
        "1883\n" +
        "1936\n" +
        "1626\n" +
        "1497\n" +
        "1860\n" +
        "1673\n" +
        "1295\n" +
        "2005\n" +
        "1481\n" +
        "1995\n" +
        "1734\n" +
        "1646\n" +
        "1557\n" +
        "1298\n" +
        "1174\n" +
        "1894\n" +
        "1309\n" +
        "1240\n" +
        "1982\n" +
        "1414\n" +
        "1799\n" +
        "1620\n" +
        "1863\n" +
        "1220\n" +
        "1642\n" +
        "508\n" +
        "1146\n" +
        "1187\n" +
        "1253\n" +
        "1284\n" +
        "1952\n" +
        "1527\n" +
        "1610\n" +
        "1333\n" +
        "1221\n" +
        "1362\n" +
        "1457\n" +
        "1721\n" +
        "1493\n" +
        "1330\n" +
        "156\n" +
        "1647\n" +
        "1841\n" +
        "1724\n" +
        "2000\n" +
        "1398\n" +
        "1745\n" +
        "1985\n" +
        "1269\n" +
        "1722\n" +
        "2001\n" +
        "1923\n" +
        "1395\n" +
        "1094\n" +
        "1140\n" +
        "1958\n" +
        "1239\n" +
        "1336\n" +
        "1588\n" +
        "1338\n" +
        "1750\n" +
        "1757\n" +
        "1444\n" +
        "1822\n" +
        "1335\n" +
        "1941\n" +
        "1865\n" +
        "1767\n" +
        "1881\n" +
        "1499\n" +
        "1157\n" +
        "1990\n" +
        "1210\n" +
        "1779\n" +
        "1201\n" +
        "1784\n" +
        "1961\n" +
        "1476\n" +
        "1861\n" +
        "1468"