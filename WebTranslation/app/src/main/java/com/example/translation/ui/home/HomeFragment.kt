package com.example.translation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.translation.databinding.FragmentHomeBinding
import com.example.translation.ui.webtranslate.TranslateFragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        user_name.setText("사용자 이름")
        /*
        translateButton.setOnClickListener {
            (activity as MainActivity).getTranslateService()
            koreanString.setText(translate(englishString.text.toString()))
        }

         */
    }

    /*
    public fun translate(text : String): String {
        var resultText : String = ""
        val translation = (activity as MainActivity).translate?.translate(text, Translate.TranslateOption.targetLanguage("ko"), Translate.TranslateOption.model("base"))
        if (translation != null) {
            //koreanString!!.setText(translation.translatedText)
            resultText = translation.translatedText
        }
        //Toast.makeText(activity,originalText,Toast.LENGTH_SHORT).show()
        return resultText
    }
     */

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}