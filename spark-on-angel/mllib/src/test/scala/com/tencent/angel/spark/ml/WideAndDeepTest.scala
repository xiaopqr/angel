package com.tencent.angel.spark.ml

import com.tencent.angel.RunningMode
import com.tencent.angel.conf.AngelConf
import com.tencent.angel.ml.core.conf.{MLCoreConf, SharedConf}
import com.tencent.angel.ml.math2.utils.RowType

import com.tencent.angel.spark.ml.classification.WideAndDeep
import com.tencent.angel.spark.ml.core.OfflineLearner
import org.apache.log4j.PropertyConfigurator
import org.apache.spark.{SparkConf, SparkContext}


class WideAndDeepTest extends PSFunSuite with SharedPSContext {
  private var learner: OfflineLearner = _
  private var input: String = _
  private var dim: Int = _

  override def beforeAll(): Unit = {
    super.beforeAll()

    input = "../../data/census/census_148d_train.libsvm"

    // build SharedConf with params
    SharedConf.get()
    SharedConf.get().set(MLCoreConf.ML_MODEL_TYPE, RowType.T_FLOAT_DENSE.toString)
    SharedConf.get().setInt(MLCoreConf.ML_FEATURE_INDEX_RANGE, 149)
    SharedConf.get().setDouble(MLCoreConf.ML_LEARN_RATE, 0.5)
    SharedConf.get().set(MLCoreConf.ML_DATA_INPUT_FORMAT, "libsvm")
    SharedConf.get().setInt(MLCoreConf.ML_EPOCH_NUM, 5)
    SharedConf.get().setDouble(MLCoreConf.ML_VALIDATE_RATIO, 0.1)
    SharedConf.get().setDouble(MLCoreConf.ML_REG_L2, 0.0)
    SharedConf.get().setDouble(MLCoreConf.ML_BATCH_SAMPLE_RATIO, 0.2)
    dim = SharedConf.indexRange.toInt

    SharedConf.get().set(AngelConf.ANGEL_RUNNING_MODE, RunningMode.ANGEL_PS.toString)
    learner = new OfflineLearner

  }

  override def afterAll(): Unit = {
    super.afterAll()
  }

  test("WideAndDeep") {
    SharedConf.get().setLong(MLCoreConf.ML_FIELD_NUM, 13)
    SharedConf.get().setLong(MLCoreConf.ML_RANK_NUM, 5)
    val model = new WideAndDeep
    learner.train(input, "", "", dim, model)
  }

}
