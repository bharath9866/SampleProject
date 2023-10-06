package com.example.sampleproject

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.ConstraintSet.WRAP_CONTENT
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.sampleproject.databinding.ActivityMainBinding
import com.example.sampleproject.databinding.LearnerWelcomeDialogueBinding
import com.example.sampleproject.ui.theme.SampleProjectTheme

private const val BLUR_RADIUS = 25f

fun Modifier.advancedShadow(
    color: Color = Color.Black,
    alpha: Float = 1f,
    cornersRadius: Dp = 0.dp,
    shadowBlurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
) = drawBehind {

    val shadowColor = color.copy(alpha = alpha).toArgb()
    val transparentColor = color.copy(alpha = 0f).toArgb()

    drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparentColor
        frameworkPaint.setShadowLayer(
            shadowBlurRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            shadowColor
        )
        it.drawRoundRect(
            0f,
            0f,
            this.size.width,
            this.size.height,
            cornersRadius.toPx(),
            cornersRadius.toPx(),
            paint
        )
    }
}

infix fun LearnerWelcomeDialogueBinding.invokeLearnerScoreWelcomePage(applicationContext: Context) {

    val INTRO_SLIDER_STATIC_LIST = arrayListOf(
        IntroSlide(
            "Weekly Insights",
            "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam",
            R.drawable.learner_welcome_1_img
        ),
        IntroSlide(
            "Plan your Goal",
            "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam",
            R.drawable.learner_welcome_2_img
        ),
        IntroSlide(
            "Learning Stages",
            "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam",
            R.drawable.learner_welcome_3_img
        )
    )

    val binding = this
    val handler = Handler(Looper.myLooper()!!)
    val introSliderAdapter = IntroSliderAdapter(INTRO_SLIDER_STATIC_LIST, binding.introSliderViewPager)
    binding.learnerScoreWelcomeDialogue[0].overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    binding.introSliderViewPager.adapter = introSliderAdapter

    val runnable = Runnable {
        if (binding.introSliderViewPager.currentItem == INTRO_SLIDER_STATIC_LIST.lastIndex)
            binding.introSliderViewPager.currentItem = 0
        else
            binding.introSliderViewPager.currentItem += 1
    }

    fun setCurrentIndicator(index: Int) {
        for (i in 0 until binding.indicatorsContainer.childCount) {
            val imageView = binding.indicatorsContainer[i] as ImageView
            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.learner_indicator_active))
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.learner_indicator_inactive))
            }
        }
    }

    binding.introSliderViewPager.registerOnPageChangeCallback(
        object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 2000)
            }
        }
    )


    fun setupIndicator() {
        val indicator = arrayOfNulls<ImageView>(introSliderAdapter?.itemCount?:0)
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicator.indices) {
            indicator[i] = ImageView(applicationContext)
            indicator[i].apply {
                this?.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.learner_indicator_inactive))
                this?.layoutParams = layoutParams
            }
            binding.indicatorsContainer.addView(indicator[i])
        }
    }
    setupIndicator()
}

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding
    private var textList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        if (intent != null) {
            addToList()
            setUpRecyclerView()
            setUpFab(this@MainActivity)
//            showCustomDialogBox()
            binding.learnerFloatingBarInclude.learnerMotionLayoutFloatingBar.setOnClickListener {
//                showCustomDialogBox()
            }
        }

    }

    private fun showCustomDialogBox(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        val dialogBinding = LearnerWelcomeDialogueBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.Transparent.toArgb()))

        val closeBtn:ImageView = dialog.findViewById(R.id.learnerWelcomePageCloseButton)

        dialog.show()

        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding invokeLearnerScoreWelcomePage applicationContext

    }

    /*fun setUpTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }
        binding.introSliderViewPager.setPageTransformer(transformer)
    }*/

    @SuppressLint("RestrictedApi", "ClickableViewAccessibility")
    private fun setUpFab(context: Context) {

        binding.learnerFloatingBarInclude.learnerPerformanceActivityBarBgId.visibility = View.GONE
        binding.learnerFloatingBarInclude.learnerCollapsedFloatingBarId.visibility = View.GONE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            binding.learnerFloatingBarInclude.learnerActivityId
//            binding.learnerFloatingBarInclude.learnerPerformanceActivityBarBgId.setRenderEffect(RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.MIRROR))
//            binding.learnerFloatingBarInclude.learnerGlossyBgId.alpha = 0.4f
//            binding.learnerFloatingBarInclude.learnerGlossyBgId.imageAlpha = 125
        }

        binding.learnerFloatingBarInclude.learnerExpandedBarCircleProgressInclude.learnerCircleProgressView.progressValue = 60
        binding.learnerFloatingBarInclude.learnerExpandedBarCircleProgressInclude.learnerCircleProgressView.progressValue2 = 75

        binding.learnerFloatingBarInclude.learnerPerformanceActivityBarBgId.setOnClickListener {
            Log.d("onClick", "learnerPerformanceActivityBarBgId")
        }

        binding.learnerFloatingBarInclude.learnerCollapsedFloatingBarId.setOnClickListener {
            Log.d("onClick", "learnerCollapsedFloatingBarId")
        }

//        binding.rvRecycler.addOnScrollListener(
//            object: RecyclerView.OnScrollListener() {
//                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                    val maxScrollY = recyclerView.computeVerticalScrollRange() - recyclerView.height
//                    Log.d("maxScrollY", "$maxScrollY")
//
//                    val requiredPercent = 0.25 * maxScrollY
//                    Log.d("25%Scroll", "$requiredPercent")
//
//                    val isBelowPercent = recyclerView.computeVerticalScrollOffset()/requiredPercent
//                    Log.d("isBelowPercent", "$isBelowPercent")
//
//                    Log.d("dy", "${recyclerView.computeVerticalScrollOffset()}")
//                    binding.learnerFloatingBarInclude.learnerMotionLayoutFloatingBar.progress = if(isBelowPercent.toFloat()<=0.9f) isBelowPercent.toFloat() else 1f
//                    if(binding.learnerFloatingBarInclude.learnerMotionLayoutFloatingBar.progress >= 1) {
//                        binding.learnerFloatingBarInclude.learnerPerformanceActivityBarBgId.isClickable = false
//                        binding.learnerFloatingBarInclude.learnerCollapsedFloatingBarId.isClickable = true
//                    } else {
//                        binding.learnerFloatingBarInclude.learnerCollapsedFloatingBarId.isClickable = false
//                        binding.learnerFloatingBarInclude.learnerPerformanceActivityBarBgId.isClickable = true
//                    }
//                }
//            }
//        )

        binding.nestedScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldSrollX, oldScrollY ->
            val maxScrollY = binding.nestedScrollView.getChildAt(0).height - binding.nestedScrollView.height
            Log.d("learnerScore", "maxScrollY: $maxScrollY")

            val requiredPercent = 0.25 * maxScrollY
            Log.d("learnerScore", "25%Scroll: ${0.25 * maxScrollY}")

            val twentyFivePercent = scrollY / requiredPercent
            Log.d("learnerScore", "isBelowPercent: $twentyFivePercent")

            Log.d("learnerScore", "scrollY: $scrollY")
            Log.d("touch", "progress: ${binding.learnerFloatingBarInclude.learnerMotionLayoutFloatingBar.progress}")
            Log.d("touch", "condition: (${twentyFivePercent.toFloat()} <= 0.9f) = ${twentyFivePercent.toFloat() <= 0.9f}")

            if (twentyFivePercent.toFloat() <= 0.9f) {
                binding.nestedScrollView.setOnTouchListener { _, motionEvent ->
                    if (motionEvent.action == MotionEvent.ACTION_UP) {
                        val temp = if (twentyFivePercent.toFloat() <= 0.21f) 0f else 1f
                        Log.d("touch", "Action_UP: $temp")
                        binding.learnerFloatingBarInclude.learnerMotionLayoutFloatingBar.progress = temp
                        true
                    } else {
                        val temp = if (twentyFivePercent.toFloat() <= 0.9f) twentyFivePercent.toFloat() else 1f
                        Log.d("touch", "Action_Other: $temp")
                        binding.learnerFloatingBarInclude.learnerMotionLayoutFloatingBar.progress = temp
                        false
                    }
                }
            }
            else
                binding.learnerFloatingBarInclude.learnerMotionLayoutFloatingBar.progress = 1f


            if (binding.learnerFloatingBarInclude.learnerMotionLayoutFloatingBar.progress >= 1) {
                binding.learnerFloatingBarInclude.learnerPerformanceActivityBarBgId.isClickable = false
                binding.learnerFloatingBarInclude.learnerCollapsedFloatingBarId.isClickable = true
            } else {
                binding.learnerFloatingBarInclude.learnerCollapsedFloatingBarId.isClickable = false
                binding.learnerFloatingBarInclude.learnerPerformanceActivityBarBgId.isClickable = true
            }
        }

    }

    private fun setUpRecyclerView() {
        /*binding.rvRecycler.layoutManager = LinearLayoutManager(applicationContext)
        binding.rvRecycler.adapter = RecyclerAdapter(textList)*/

    }


    private fun addToList() {
        val numberOfColors = 50 // Change this to the number of random colors you want
//        val randomColorList = generateRandomColorList(numberOfColors)

        for (number in 0..numberOfColors) {
            textList.add("Sample text ${number}, binding.rvRecycler.scrollY binding")
        }

//        for ((color,index) in randomColorList.withIndex()) {
//            textList.add("Sample text ${index + 1}, binding.rvRecycler.scrollY binding")
//            cardColor.add(color)
//        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SampleProjectTheme {
        Greeting("Android")
    }
}