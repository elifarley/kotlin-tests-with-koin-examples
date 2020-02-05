package koinexample

import io.kotlintest.koin.KoinListener
import io.kotlintest.shouldBe
import io.kotlintest.specs.FreeSpec
import org.koin.core.inject
import org.koin.dsl.module
import org.koin.test.KoinTest

data class Stats(var ok: Long = 0, var error: Long = 0)

interface StatsServer {
    fun newError(): Long
}

class StatsServerSimple(private val stats: Stats) : StatsServer {
    override fun newError() = stats.error++
}

val appModule = module {
    single { Stats() }
    single { StatsServerSimple(get()) as StatsServer }
}

class KoinSampleTests : FreeSpec(), KoinTest {

    private val modules = listOf(
        appModule
    )

    override fun listeners() = listOf(KoinListener(modules))

    val statsServer: StatsServer by inject()

    init {

        "Happy path" {
            statsServer.newError() shouldBe 1
        }
    }
}
