package com.synac.agecalculator.presentation.settings

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.synac.agecalculator.R
import com.synac.agecalculator.presentation.settings.component.SettingsItemCard
import com.synac.agecalculator.presentation.util.AppTheme
import com.synac.agecalculator.presentation.util.Constants
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreenRoot(
    navigateToPrivacyPolicy: (String) -> Unit,
    navigateUp: () -> Unit,
    onAppVersionClick: () -> Unit
) {
    val viewModel = koinViewModel<SettingsViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current

    SettingsScreen(
        state = state,
        onAction = { action ->
            when (action) {
                SettingAction.PrivacyPolicyClick -> {
                    navigateToPrivacyPolicy(Constants.PRIVACY_POLICY_LINK)
                }
                SettingAction.GithubLinkClick -> {
                    uriHandler.openUri(Constants.GITHUB_LINK)
                }
                SettingAction.AppVersionClick -> onAppVersionClick()
                SettingAction.OnBackClick -> navigateUp()
                is SettingAction.ChangeAppTheme -> viewModel.onThemeChanged(action.theme)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    state: SettingsUiState,
    onAction: (SettingAction) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(
                    onClick = { onAction(SettingAction.OnBackClick) }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )
        ThemeSettingsItem(
            modifier = Modifier.padding(horizontal = 12.dp),
            theme = state.appTheme,
            onClick = {
                val newThemeValue = when (state.appTheme) {
                    AppTheme.AUTO -> AppTheme.LIGHT
                    AppTheme.LIGHT -> AppTheme.DARK
                    AppTheme.DARK -> AppTheme.AUTO
                }
                onAction(SettingAction.ChangeAppTheme(newThemeValue))
            }
        )
        Text(
            text = "About",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp)
        )
        SettingsItemCard(
            modifier = Modifier.padding(horizontal = 12.dp),
            title = "App Version",
            iconResId = R.drawable.ic_code_filled,
            onClick = { onAction(SettingAction.AppVersionClick) },
            trailingContent = {
                Text(
                    text = state.appVersion,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        SettingsItemCard(
            modifier = Modifier.padding(horizontal = 12.dp),
            title = "Project On Github",
            iconResId = R.drawable.ic_github,
            onClick = { onAction(SettingAction.GithubLinkClick) }
        )
        Spacer(modifier = Modifier.height(12.dp))
        SettingsItemCard(
            modifier = Modifier.padding(horizontal = 12.dp),
            title = "Privacy Policy",
            iconResId = R.drawable.ic_privacy_tip_filled,
            onClick = { onAction(SettingAction.PrivacyPolicyClick) }
        )
    }
}

@Composable
private fun ThemeSettingsItem(
    modifier: Modifier = Modifier,
    theme: AppTheme,
    onClick: () -> Unit
) {
    val themeText = remember(theme) {
        when (theme) {
            AppTheme.LIGHT -> "Light"
            AppTheme.DARK -> "Dark"
            else -> "Auto"
        }
    }
    val themeIconResId = remember(theme) {
        when (theme) {
            AppTheme.LIGHT -> R.drawable.ic_light_mode
            AppTheme.DARK -> R.drawable.ic_dark_mode
            else -> R.drawable.ic_theme_auto
        }
    }

    SettingsItemCard(
        modifier = modifier,
        iconResId = R.drawable.ic_paint_roller_filled,
        title = "App Theme",
        onClick = onClick
    ) {
        AnimatedContent(themeText, label = "themeTex") { text ->
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        AnimatedContent(themeIconResId, label = "themePainter") { icon ->
            Icon(
                painter = painterResource(icon),
                contentDescription = themeText,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSettingsScreen() {
    SettingsScreen(
        state = SettingsUiState(),
        onAction = {}
    )
}