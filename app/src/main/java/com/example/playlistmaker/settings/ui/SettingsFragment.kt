package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.playlistmaker.R
import com.example.playlistmaker.app.App
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.ui.presentation.SettingsViewModel
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel

const val SWITCH_PREFERENCES = "practicum_example_preferences"
const val SWITCH_IS_CHECKED = "switch_is_checked"

class SettingsFragment : Fragment() {

    private val settingsViewModel by viewModel<SettingsViewModel>()
    private lateinit var binding: FragmentSettingsBinding

    private lateinit var themeSwitcher: SwitchMaterial
    private lateinit var layoutShare: LinearLayout
    private lateinit var layoutSupport: LinearLayout
    private lateinit var layoutAgreement: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        themeSwitcher = binding.switchTheme
        layoutShare = binding.LayoutShare
        layoutSupport = binding.LayoutSupport
        layoutAgreement = binding.LayoutAgreement

        //Подписка на изменения во ViewModel
        //смотрим переключатель темы
        settingsViewModel.isCheckedThemeLive.observe(
            viewLifecycleOwner,
            Observer { isChecked ->
                themeSwitcher.isChecked = isChecked
            }
        )

        //интент для отправки ссылки с выбором приложения
        layoutShare.setOnClickListener{
            settingsViewModel.shareLink(requireContext().getString(R.string.android_developer_link))
        }

        //интент для отправки сообщения по электронной почте
        layoutSupport.setOnClickListener{
            settingsViewModel.emailSupport(
                requireContext().getString(R.string.student_mail),
                requireContext().getString(R.string.topic_mail),
                requireContext().getString(R.string.message_mail)
            )
        }

        //интент для запуска браузера с веб страницей
        layoutAgreement.setOnClickListener{
            settingsViewModel.agreementLink(requireContext().getString(R.string.agreement_link))
        }

        //Cохранение переключателя темы
        themeSwitcher.setOnCheckedChangeListener { switcher, isChecked ->
            (requireContext().applicationContext as App).switchTheme(isChecked)
            settingsViewModel.saveTheme(SWITCH_PREFERENCES, SWITCH_IS_CHECKED,isChecked)
        }

    }

    //Обработка кнопки Назад
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == android.R.id.home) finish()
//        return true
//    }
}