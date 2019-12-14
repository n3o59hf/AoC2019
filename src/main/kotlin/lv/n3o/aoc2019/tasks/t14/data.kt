package lv.n3o.aoc2019.tasks.t14

import lv.n3o.aoc2019.tasks.IO

private val data14 = """
    3 QVSV => 2 WXRQ
    1 KXSC, 2 PSBCN, 11 DNCJV, 2 FTCT, 1 BGMC => 7 PTHL
    1 PXFX => 1 LBZJ
    2 WXRQ, 12 ZSCZD => 2 HLQM
    1 HDTJ, 1 LBZJ, 1 SLPCX, 5 SMCGZ, 3 MFMX, 4 CHZT, 12 BKBCB => 1 HRNSK
    10 WSNDR, 1 JCBJ, 3 LBZJ => 2 QBTSV
    22 LHZDG, 5 WFXH => 4 XTQRH
    1 HLQM => 3 WSNDR
    4 NTJCX => 6 TVMCM
    1 VDSW, 9 SLPCX => 1 QCMX
    2 MFMX => 8 NTJCX
    154 ORE => 4 BSTS
    12 TKML => 7 FWTFH
    14 VDSW, 7 FVGK, 2 JCBJ => 4 LVFB
    15 PLGZ, 27 FTCT, 1 LVFB => 4 TNGFX
    2 WHPJT, 20 FXPHZ => 7 PQKMJ
    6 NJWBT, 8 KVTD, 1 LQFW => 4 ZCDCW
    1 QVSV, 2 FXPHZ => 5 ZSCZD
    16 LRNQK => 6 BKBCB
    5 FXPHZ => 1 FVGK
    2 PXFX => 5 CHZT
    17 SMZS, 1 VDSW, 7 BSTS => 5 SLPCX
    9 RXJQJ, 2 ZVTW, 1 JMDT => 8 BGMC
    5 PXFX, 1 FVGK, 2 TGHSD => 2 LRNQK
    13 JMDT, 1 BHRFW, 32 MCKPL => 5 KXSC
    5 CBZMB => 8 BLTD
    3 KVTD, 2 LQFW, 1 LBZJ => 5 NJWBT
    1 MCKPL, 2 CHZT, 6 TKML => 6 JCBJ
    1 JSBS => 9 TGHSD
    6 RXJQJ, 20 LRNQK, 29 KVTD => 8 PLGZ
    18 WHPJT => 3 SMCGZ
    157 ORE => 8 PNFB
    9 QBTSV, 1 LFRF, 2 TNGFX, 4 FTCT, 9 QCMX, 4 PSBCN, 14 ZCDCW, 1 TVMCM => 7 CKQG
    8 WHPJT => 9 LFRF
    5 VDSW, 24 FWTFH => 1 JMDT
    2 WXRQ, 4 BLTD => 7 WHPJT
    14 VDSW => 3 CBZMB
    1 QCMX, 19 BHRFW, 2 NJWBT => 3 FTCT
    3 XTQRH => 2 KVTD
    5 QBTSV, 2 JMDT, 3 LVFB => 3 HDTJ
    16 PQKMJ, 1 WSNDR => 5 DNCJV
    1 CBZMB, 2 PTHL, 6 HRNSK, 80 WHPJT, 10 CKQG, 55 ZVTW => 1 FUEL
    5 BKBCB, 3 WSNDR => 1 MCKPL
    158 ORE => 3 LHZDG
    1 HLQM, 1 ZSCZD => 2 VDSW
    140 ORE => 6 QVSV
    4 ZSCZD, 13 TGHSD => 1 TKML
    1 SLPCX, 3 TKML => 2 HWDQZ
    1 BSTS, 8 WXRQ => 5 LQFW
    3 BGMC, 3 LRNQK, 3 QBTSV => 6 PSBCN
    1 PNFB => 4 FXPHZ
    8 WXRQ => 7 JSBS
    1 WXRQ, 8 PNFB, 3 XTQRH => 9 PXFX
    1 WSNDR, 13 JSBS, 1 VDSW => 8 SMZS
    6 NJWBT => 4 BHRFW
    1 PXFX, 11 JSBS => 5 RXJQJ
    103 ORE => 2 WFXH
    5 WHPJT, 6 LRNQK => 2 MFMX
    32 HWDQZ, 1 JMDT => 5 ZVTW
""".trimIndent()
private val answer14a = "870051"
private val answer14b = "1863741"

class IO : IO() {
    override val input = data14
    override val testA = answer14a
    override val testB = answer14b
}