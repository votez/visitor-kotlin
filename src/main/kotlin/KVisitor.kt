// could be sealed class
abstract class Expression: Visitable

interface Visitable {
    fun <R> accept(visitor: Visitor<R>) : R
}
interface Visitor<R> {
    fun visit(c: Constant) : R
    fun visit(o: PlusOperator) : R
}

class Constant (val number: Int): Expression() , Visitable {
    override fun <R> accept(visitor: Visitor<R>): R = visitor.visit(this)
}

class PlusOperator(val left: Expression, val right: Expression) : Expression(), Visitable {
    override fun <R> accept(visitor: Visitor<R>): R = visitor.visit(this)
}

class Evaluator {
    fun evaluate(expression: Expression) : Int = when (expression) {
        is Constant -> expression.number
        is PlusOperator -> evaluate(expression.left) + evaluate(expression.left)
        else -> error("Oooops")
    }
}

class LispPrinter {
    fun evaluate(expression: Expression) : String = when (expression) {
        is Constant -> "${expression.number}"
        is PlusOperator -> "( + ${evaluate(expression.left)}, ${evaluate(expression.right)})"
        else -> error("Oooops")
    }
}

class EvaluatorVisitor: Visitor<Int> {
    override fun visit(c: Constant): Int = c.number

    override fun visit(o: PlusOperator): Int = o.left.accept(this) + o.right.accept(this)
}

class LispPrinterVisitor: Visitor<String> {
    override fun visit(c: Constant): String = "${c.number}"

    override fun visit(o: PlusOperator): String = "( + ${o.left.accept(this)}, ${o.right.accept(this)})"
}

fun main() {
    val e = PlusOperator( Constant(5), PlusOperator( Constant(2), Constant(3)))
    println(Evaluator().evaluate(e))
    println(e.accept(EvaluatorVisitor()))
    println(LispPrinter().evaluate(e))
    println(e.accept(LispPrinterVisitor()))

}