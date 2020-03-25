package visitor

import Constant
import Evaluator
import EvaluatorVisitor
import LispPrinter
import LispPrinterVisitor
import PlusOperator
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import org.openjdk.jmh.runner.Runner
import org.openjdk.jmh.runner.options.OptionsBuilder
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue
import kotlin.random.Random

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Warmup(iterations = 5)
@Measurement(iterations = 3, time = 50, timeUnit = TimeUnit.SECONDS)
@Fork(1)
open class VisitorBenchmark {
    private val r = Random.Default
    private val indices = Array(20) {
        r.nextInt().absoluteValue % 10
    }
    private val expression = Array(10) {
        PlusOperator(Constant(r.nextInt(100)), PlusOperator(Constant(r.nextInt(100)), Constant(r.nextInt(100))))
    }
    private val lispPrinter = LispPrinter()
    private val evaluator = Evaluator()
    private val lispPrinterVisitor = LispPrinterVisitor()
    private val evaluatorVisitor = EvaluatorVisitor()

/*
    @Benchmark
    fun lispWhen(sink: Blackhole) = indices.forEach {
        sink.consume(lispPrinter.evaluate(expression[it]))
    }


    @Benchmark
    fun lispVisitor(sink: Blackhole) = indices.forEach {
        sink.consume(expression[it].accept(lispPrinterVisitor))
    }
*/

    @Benchmark
    fun evaluateWhen(sink: Blackhole) = indices.forEach {
        sink.consume(evaluator.evaluate(expression[it]))
    }

    @Benchmark
    fun evaluateVisitor(sink: Blackhole) = indices.forEach {
        sink.consume(expression[it].accept(evaluatorVisitor))
    }

    fun main() {
        val opt = OptionsBuilder().include(VisitorBenchmark::class.java.simpleName)
            .forks(1)
            .build()
        Runner(opt).run()
    }
}