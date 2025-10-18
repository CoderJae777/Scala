object sandbox extends App {
  def curriedTax(rate: Double)(price: Double): Double = price * rate
  val gst_with_rate = curriedTax(0.08)(_: Double)
  val gst_with_price = curriedTax(_: Double)(100)
}
