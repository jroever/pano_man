package panoman

import spinal.core._
import spinal.lib.Counter
import spinal.lib.CounterFreeRun
import spinal.lib.GrayCounter
import spinal.lib.master
import spinal.lib.io.{ReadableOpenDrain, TriStateArray, TriState}

import mr1._

class PanoCore extends Component {

    val io = new Bundle {
        val led_green           = out(Bool)
        val led_blue            = out(Bool)
        val led1    = out(Bool)

        val codec_scl = master(ReadableOpenDrain(Bool))
        val codec_sda = master(ReadableOpenDrain(Bool))

        val vo_scl = master(ReadableOpenDrain(Bool))
        val vo_sda = master(ReadableOpenDrain(Bool))
        val gpio_out = out Bits(18 bits)

        val usb_a   = out(UInt(17 bits))
        val usb_d   = master(TriStateArray(16 bits))
        val usb_cs_ = out(Bool)
        val usb_rd_ = out(Bool)
        val usb_wr_ = out(Bool)
        val usb_irq = in(Bool)

    }

    val leds = new Area {
        val led_cntr = Reg(UInt(24 bits)) init(0)

        when(led_cntr === U(led_cntr.range -> true)){
            led_cntr := 0
        }
        .otherwise {
            led_cntr := led_cntr +1
        }

        io.led_green    := led_cntr.msb
    }

    val eof_final = Bool

    val mr1Config = MR1Config()
    val u_mr1_top = new MR1Top(mr1Config)
    u_mr1_top.io.led1       <> io.led_blue
    u_mr1_top.io.switch_    <> True
    u_mr1_top.io.codec_scl  <> io.codec_scl
    u_mr1_top.io.codec_sda  <> io.codec_sda
    u_mr1_top.io.vo_scl     <> io.vo_scl
    u_mr1_top.io.vo_sda     <> io.vo_sda
    u_mr1_top.io.led1       <> io.led1
    u_mr1_top.io.gpio_out  <> io.gpio_out
    u_mr1_top.io.usb_a     <> io.usb_a
    u_mr1_top.io.usb_d     <> io.usb_d
    u_mr1_top.io.usb_cs_   <> io.usb_cs_
    u_mr1_top.io.usb_rd_   <> io.usb_rd_
    u_mr1_top.io.usb_wr_   <> io.usb_wr_


}


