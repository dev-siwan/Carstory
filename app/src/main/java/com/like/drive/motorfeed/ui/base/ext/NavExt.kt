package com.like.drive.motorfeed.ui.base.ext

import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigator

fun NavController.getCurrentFragmentClassName() =
    (currentDestination as FragmentNavigator.Destination).className