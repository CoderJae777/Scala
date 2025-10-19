object sandbox extends App {
  // def curriedTax(rate: Double)(price: Double): Double = price * rate
  // val gst_with_rate = curriedTax(0.08)(_: Double)
  // val gst_with_price = curriedTax(_: Double)(100)

// Define a binary tree
  sealed trait Tree[+A]
  case class Leaf[A](value: A) extends Tree[A]
  case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

// Implement fold for Tree
  def foldTree[A, B](tree: Tree[A], acc: B)(f: (B, A) => B): B = tree match {
    case Leaf(value) => f(acc, value)
    case Branch(l, r) =>
      val leftFolded = foldTree(l, acc)(f)
      foldTree(r, leftFolded)(f)
  }

// Example usage: sum all values in a tree
  val tree: Tree[Int] = Branch(Leaf(1), Branch(Leaf(2), Leaf(5)))
  val sum = foldTree(tree, 0)(_ + _)
  println(sum)
}
