package ge.asurguladze.finalproject.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ge.asurguladze.finalproject.fragments.MainPageFragment
import ge.asurguladze.finalproject.fragments.ProfilePageFragment

class ViewPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity){

    private var fragmentsList = arrayListOf(MainPageFragment(), ProfilePageFragment())

    override fun getItemCount(): Int {
        return  fragmentsList.size
    }

    override fun createFragment(position: Int): Fragment {
        return  fragmentsList[position]
    }

}