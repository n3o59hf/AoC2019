package lv.n3o.aoc2019.tasks.t09

import lv.n3o.aoc2019.tasks.IO

private val data09 =
    "1102,34463338,34463338,63,1007,63,34463338,63,1005,63,53,1102,3,1,1000,109,988,209,12,9,1000,209,6,209,3,203,0,1008,1000,1,63,1005,63,65,1008,1000,2,63,1005,63,904,1008,1000,0,63,1005,63,58,4,25,104,0,99,4,0,104,0,99,4,17,104,0,99,0,0,1101,0,31,1019,1101,25,0,1008,1102,35,1,1009,1102,422,1,1029,1102,1,21,1005,1102,1,734,1027,1102,29,1,1000,1101,32,0,1018,1102,28,1,1016,1101,0,38,1015,1101,0,378,1023,1101,30,0,1017,1102,1,381,1022,1101,0,37,1006,1102,1,1,1021,1101,0,24,1011,1102,1,23,1002,1101,0,0,1020,1101,0,20,1007,1101,427,0,1028,1101,26,0,1014,1101,27,0,1010,1101,0,39,1001,1101,34,0,1012,1102,1,36,1013,1101,0,33,1003,1101,804,0,1025,1101,737,0,1026,1102,1,809,1024,1102,1,22,1004,109,9,1201,-7,0,63,1008,63,20,63,1005,63,205,1001,64,1,64,1106,0,207,4,187,1002,64,2,64,109,2,21102,40,1,1,1008,1012,40,63,1005,63,233,4,213,1001,64,1,64,1106,0,233,1002,64,2,64,109,4,1208,-7,25,63,1005,63,255,4,239,1001,64,1,64,1106,0,255,1002,64,2,64,109,-24,1207,10,38,63,1005,63,271,1105,1,277,4,261,1001,64,1,64,1002,64,2,64,109,25,21107,41,40,-3,1005,1013,293,1105,1,299,4,283,1001,64,1,64,1002,64,2,64,109,5,1205,-1,311,1106,0,317,4,305,1001,64,1,64,1002,64,2,64,109,-23,1202,6,1,63,1008,63,22,63,1005,63,339,4,323,1105,1,343,1001,64,1,64,1002,64,2,64,109,1,2101,0,2,63,1008,63,37,63,1005,63,367,1001,64,1,64,1106,0,369,4,349,1002,64,2,64,109,29,2105,1,-5,1106,0,387,4,375,1001,64,1,64,1002,64,2,64,109,-26,2101,0,0,63,1008,63,23,63,1005,63,409,4,393,1106,0,413,1001,64,1,64,1002,64,2,64,109,26,2106,0,0,4,419,1106,0,431,1001,64,1,64,1002,64,2,64,109,-17,21108,42,42,6,1005,1017,453,4,437,1001,64,1,64,1106,0,453,1002,64,2,64,109,7,21101,43,0,-8,1008,1010,44,63,1005,63,477,1001,64,1,64,1105,1,479,4,459,1002,64,2,64,109,-7,1206,10,495,1001,64,1,64,1106,0,497,4,485,1002,64,2,64,109,-5,2108,36,0,63,1005,63,513,1106,0,519,4,503,1001,64,1,64,1002,64,2,64,109,3,2102,1,-5,63,1008,63,22,63,1005,63,541,4,525,1105,1,545,1001,64,1,64,1002,64,2,64,109,3,1207,-6,38,63,1005,63,567,4,551,1001,64,1,64,1105,1,567,1002,64,2,64,109,-15,2107,20,8,63,1005,63,585,4,573,1106,0,589,1001,64,1,64,1002,64,2,64,109,-1,1208,5,36,63,1005,63,609,1001,64,1,64,1106,0,611,4,595,1002,64,2,64,109,30,21101,44,0,-7,1008,1019,44,63,1005,63,633,4,617,1106,0,637,1001,64,1,64,1002,64,2,64,109,-25,1201,0,0,63,1008,63,39,63,1005,63,659,4,643,1105,1,663,1001,64,1,64,1002,64,2,64,109,27,1206,-8,677,4,669,1106,0,681,1001,64,1,64,1002,64,2,64,109,-28,2108,29,0,63,1005,63,703,4,687,1001,64,1,64,1106,0,703,1002,64,2,64,109,5,21107,45,46,7,1005,1012,725,4,709,1001,64,1,64,1106,0,725,1002,64,2,64,109,30,2106,0,-8,1105,1,743,4,731,1001,64,1,64,1002,64,2,64,109,-22,21102,46,1,4,1008,1017,44,63,1005,63,767,1001,64,1,64,1105,1,769,4,749,1002,64,2,64,109,-15,1202,10,1,63,1008,63,23,63,1005,63,793,1001,64,1,64,1106,0,795,4,775,1002,64,2,64,109,19,2105,1,7,4,801,1105,1,813,1001,64,1,64,1002,64,2,64,109,6,1205,-2,827,4,819,1106,0,831,1001,64,1,64,1002,64,2,64,109,-20,2107,22,2,63,1005,63,851,1001,64,1,64,1106,0,853,4,837,1002,64,2,64,109,20,21108,47,44,-8,1005,1015,869,1105,1,875,4,859,1001,64,1,64,1002,64,2,64,109,-22,2102,1,4,63,1008,63,23,63,1005,63,899,1001,64,1,64,1106,0,901,4,881,4,64,99,21101,0,27,1,21102,915,1,0,1106,0,922,21201,1,28703,1,204,1,99,109,3,1207,-2,3,63,1005,63,964,21201,-2,-1,1,21101,0,942,0,1106,0,922,22101,0,1,-1,21201,-2,-3,1,21101,957,0,0,1105,1,922,22201,1,-1,-2,1105,1,968,21201,-2,0,-2,109,-3,2105,1,0"
private val answer09a = "2204990589"
private val answer09b = "50008"

class IO : IO() {
    override val input = data09
    override val testA = answer09a
    override val testB = answer09b
}