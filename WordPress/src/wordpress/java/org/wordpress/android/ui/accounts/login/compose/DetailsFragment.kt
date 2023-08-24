package org.wordpress.android.ui.accounts.login.compose

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.omh.android.auth.api.OmhAuthClient
import com.omh.android.auth.api.async.CancellableCollector
import dagger.hilt.android.AndroidEntryPoint
import org.wordpress.android.WordPress
import org.wordpress.android.databinding.FragmentDetailsBinding
import org.wordpress.android.ui.ActivityLauncher
import org.wordpress.android.ui.prefs.accountsettings.AccountSettingsViewModel
import javax.inject.Inject



@AndroidEntryPoint
class DetailsFragment : Fragment() {
    @set:Inject
    lateinit var viewModel: AccountSettingsViewModel
    @Inject
    lateinit var omhAuthClient: OmhAuthClient
    private var binding: FragmentDetailsBinding? = null
    private val cancellableCollector = CancellableCollector()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupUI()
    }

    private fun setupUI() {
        val profile = requireNotNull(omhAuthClient.getUser())
        binding?.run {
            btnLogout.setOnClickListener { logout() }
            // btnRefresh.setOnClickListener { refreshToken() }
            // btnRevoke.setOnClickListener { revokeToken() }
            tvEmail.text = profile.email
            tvName.text = profile.name
            tvSurname.text = profile.surname
        }
    }

    private fun logout() {
        val cancellable = omhAuthClient.signOut()
            .addOnSuccess { navigateToLogin() }
            .addOnFailure(::showErrorDialog)
            .execute()
        cancellableCollector.addCancellable(cancellable)
    }

    private fun navigateToLogin() {
        (activity?.application as? WordPress)?.let {
            viewModel.signOutWordPress(it)
            ActivityLauncher.showMainActivity(context, true)
        }
    }

    private fun showErrorDialog(exception: Throwable) {
        exception.printStackTrace()
        val ctx = context ?: return
        AlertDialog.Builder(ctx)
            .setTitle("An error has occurred.")
            .setMessage(exception.message)
            .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
}
