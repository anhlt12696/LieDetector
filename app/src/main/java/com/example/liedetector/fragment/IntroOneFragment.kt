package com.example.liedetector.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pgs.lie.detector.prank.test.fingerprint.scanner.databinding.FragmentIntroOneBinding


class IntroOneFragment : Fragment() {
    private lateinit var binding: FragmentIntroOneBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroOneBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTitle.isSelected = true
        binding.tvTitle.requestFocus()
    }

}