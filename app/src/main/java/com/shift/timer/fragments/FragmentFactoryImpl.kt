//package com.shift.timer.fragments
//
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentFactory
//
//class FragmentFactoryImpl : FragmentFactory(){
//    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
//        return classLoader.getFragment()?: super.instantiate(classLoader, className)
//    }
//}
//
//fun ClassLoader.getFragment(): Fragment?{
//    return FragmentType.values().firstOrNull { this::class.java.name == it.fragmentName }
//}
//
//enum class FragmentType(val fragmentName: String){
//
//}