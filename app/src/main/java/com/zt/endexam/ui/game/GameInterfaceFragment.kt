package com.zt.endexam.ui.game

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zt.endexam.R
import com.zt.endexam.SunnyWeatherApplication
import com.zt.endexam.logic.Repository
import com.zt.endexam.logic.model.GameModel.Score
import com.zt.endexam.ui.music.MusicService

/**
 * 游戏主界面
 */
class GameInterfaceFragment : Fragment(){

    companion object {
        fun newInstance() = GameInterfaceFragment()
    }

    lateinit var adapter:dialogAdapter

    //创建viewModel
    val viewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(gameViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_game_interface, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //设置默认游戏难度（简单）如果之前没有设置的情况下
        if(!Repository.isSetGameStatusSaved()) {
            Repository.saveSetGameStatus(1)
        }

        //开始游戏
        val startGameBtn = view.findViewById<Button>(R.id.startGameBtn)
        startGameBtn.setOnClickListener {
            Repository.saveGameStatus(1)
            val intent = Intent(context,GameActivity::class.java)
            startActivity(intent)
        }

        //继续游戏
        val continueGameBtn = view.findViewById<Button>(R.id.continueGameBtn)
        continueGameBtn.setOnClickListener {
            Repository.saveGameStatus(2)
            val intent = Intent(context,GameActivity::class.java)
            startActivity(intent)
        }

        //再来一局
        val reStartGameBtn = view.findViewById<Button>(R.id.reStartGameBtn)
        reStartGameBtn.setOnClickListener {
            Repository.saveGameStatus(1)
            val intent = Intent(context,GameActivity::class.java)
            startActivity(intent)
        }

        //得分详情
        val scoreGameBtn = view.findViewById<Button>(R.id.scoreGameBtn)
        scoreGameBtn.setOnClickListener {
//            val builder = AlertDialog.Builder(context)
//            builder.setTitle("游戏得分")
//                .setPositiveButton("确定",DialogInterface.OnClickListener(){ dialogInterface: DialogInterface, i: Int ->
//                    Toast.makeText(context,"dialog",Toast.LENGTH_SHORT).show()
//                })
//            builder.setCancelable(true)
//            val dialog = builder.create()
//            dialog.setCanceledOnTouchOutside(true)
//            dialog.show()
            //自定义样式替换context样式
            val context = ContextThemeWrapper(activity, R.style.customDialog)
            val dialog = androidx.appcompat.app.AlertDialog.Builder(context).create()
            // 设置可点击对话框外 对话框关闭
            dialog.setCanceledOnTouchOutside(true)
            dialog.setTitle("")
            dialog.show()
            //设置对话框大小以及位置
            val window = dialog.window
            //得到屏幕信息
            val dm = resources.displayMetrics
            val pm = window?.attributes
            //背景变暗
            pm?.dimAmount = 0.1f
            pm?.height = (dm.heightPixels * 0.8).toInt()
            pm?.width = (dm.widthPixels * 0.8).toInt()
            //设置对话框大小
            window?.attributes = pm
            //设置显示位置
            window?.setGravity(Gravity.CENTER_HORIZONTAL)
            //添加自定义布局
            window?.setContentView(R.layout.dialog)
            val layoutManager = LinearLayoutManager(context)
            val recyclerView: RecyclerView? = window?.findViewById(R.id.recyclerView_dialog)
            recyclerView?.layoutManager = layoutManager
             adapter = dialogAdapter(Repository.getSavedScore())
            recyclerView?.adapter = adapter

            val scoreClose = window?.findViewById<Button>(R.id.scoreClose)
            val scoreClear = window?.findViewById<Button>(R.id.scoreClear)

            //关闭对话框
            scoreClose?.setOnClickListener {
                dialog.dismiss()
            }

            //清空数据
            scoreClear?.setOnClickListener {
                val scoreList = Repository.getSavedScore()
                scoreList.removeAll(scoreList)
                Repository.saveScore(scoreList)
                viewModel.scoreList.value = Repository.getSavedScore()
                adapter.notifyDataSetChanged()
            }
        }

        //设置难度
        val setGameBtn = view.findViewById<Button>(R.id.setGameBtn)
        setGameBtn.setOnClickListener {
            //自定义样式替换context样式
            val context = ContextThemeWrapper(activity, R.style.customDialog)
            val dialog = androidx.appcompat.app.AlertDialog.Builder(context).create()
            // 设置可点击对话框外 对话框关闭
            dialog.setCanceledOnTouchOutside(true)
            dialog.setTitle("")
            dialog.show()
            //设置对话框大小以及位置
            val window = dialog.window
            //得到屏幕信息
            val dm = resources.displayMetrics
            val pm = window?.attributes
            //背景变暗
            pm?.dimAmount = 0.1f
            pm?.height = (dm.heightPixels * 0.8).toInt()
            pm?.width = (dm.widthPixels * 0.8).toInt()
            //设置对话框大小
            window?.attributes = pm
            //设置显示位置
            window?.setGravity(Gravity.CENTER_HORIZONTAL)
            //添加自定义布局
            window?.setContentView(R.layout.dialog_set)

            val setGamebtn_jiandan = window?.findViewById<Button>(R.id.setGamebtn_jiandan)
            val setGamebtn_nan = window?.findViewById<Button>(R.id.setGamebtn_nan)
            val setGamebtn_feichangnan = window?.findViewById<Button>(R.id.setGamebtn_feichangnan)
            val scoreClose = window?.findViewById<Button>(R.id.scoreClose)

            //简单
            setGamebtn_jiandan?.setOnClickListener {
                Repository.saveSetGameStatus(1)
                Toast.makeText(context,"成功将游戏设置为简单",Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            //难
            setGamebtn_nan?.setOnClickListener {
                Repository.saveSetGameStatus(2)
                Toast.makeText(context,"成功将游戏设置为难",Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            //非常难
            setGamebtn_feichangnan?.setOnClickListener {
                Repository.saveSetGameStatus(3)
                Toast.makeText(context,"成功将游戏设置为非常难",Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            //关闭
            scoreClose?.setOnClickListener {
                dialog.dismiss()
            }

        }

        viewModel.scoreList.observe(viewLifecycleOwner, Observer { it->
            adapter.notifyDataSetChanged()
        })
    }

}