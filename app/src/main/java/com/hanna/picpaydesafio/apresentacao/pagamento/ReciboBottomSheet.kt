package com.hanna.picpaydesafio.apresentacao.pagamento

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.hanna.picpaydesafio.R
import com.hanna.picpaydesafio.apresentacao.contatos.ContatosActivity
import com.hanna.picpaydesafio.util.ConstantesPacotes
import kotlinx.android.synthetic.main.view_recibo.view.*
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class ReciboBottomSheetFragment : BottomSheetDialogFragment() {

    private var reciboFragmentView: View? = null
    private var mContexto: Context? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        reciboFragmentView = inflater.inflate(R.layout.view_recibo, container, false)
        return reciboFragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContexto = view.context
        inicializaView()
    }

    override fun onDestroyView() {
        chamaTelaContatos()
        super.onDestroyView()
    }

    private fun chamaTelaContatos() {
        val intent = ContatosActivity.buscaIntent(mContexto!!)
        this.startActivity(intent)
    }

    @SuppressLint("SetTextI18n")
    private fun inicializaView() {
        val idTransacao = arguments?.get(ConstantesPacotes.CHAVE_TRANSACAO.ID_TRANSACAO)
        val valor = arguments?.get(ConstantesPacotes.CHAVE_TRANSACAO.VALOR_TRANSACAO)
        val timestamp = arguments?.get(ConstantesPacotes.CHAVE_TRANSACAO.TIMESTAMP_TRANSACAO)
        val usernameRecebedor = arguments?.get(ConstantesPacotes.CHAVE_TRANSACAO.USERNAME_TRANSACAO)
        val urlImagemRecebedor = arguments?.get(ConstantesPacotes.CHAVE_TRANSACAO.FOTO_TRANSACAO)
        val numeroCartao = arguments?.get(ConstantesPacotes.CHAVE_TRANSACAO.CARTAO_TRANSACAO)
        val valorEmReal = formataBigDecimalParaMoeda(valor.toString().toBigDecimal())
        val dataHoraAtual = buscaDataHoraAtual(timestamp.toString())

        with(reciboFragmentView!!) {
            tv_usernameContato.text = usernameRecebedor.toString()
            tv_numeroTransacao.text = getString(R.string.transacao) + " " + idTransacao
            tv_dadosCartao.text = getString(R.string.cartao_master) + " " + numeroCartao
            tv_valor.text = valorEmReal
            tv_totalPago.text = valorEmReal
            tv_dataHora.text = dataHoraAtual
        }
        Glide.with(reciboFragmentView!!.context).load(urlImagemRecebedor).into(reciboFragmentView!!.civ_fotoContato)
    }

    fun formataBigDecimalParaMoeda(valor: BigDecimal): String {
        val formatacaoParaReal = DecimalFormat
            .getCurrencyInstance(Locale("pt", "br"))
        return formatacaoParaReal.format(valor)
    }

    private fun buscaDataHoraAtual(timestamp: String): String {
        val calendario = Calendar.getInstance()
        calendario.timeInMillis = timestamp.toLong()
        val formatoDataHora = SimpleDateFormat("dd/MM/yyyy 'às' hh:mm", Locale("pt", "BR"))
        return formatoDataHora.format(calendario.timeInMillis)

        /*val formatoDataHora = SimpleDateFormat("dd/MM/yyyy 'às' hh:mm", Locale("pt", "BR"))
        val calendario = Calendar.getInstance()

        val ano = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val diaDoMes = calendario.get(Calendar.DAY_OF_MONTH)
        val hora = calendario.get(Calendar.HOUR_OF_DAY)
        val minuto = calendario.get(Calendar.MINUTE)

        calendario.set(ano, mes, diaDoMes, hora, minuto)
        println(calendario.time)

        return formatoDataHora.format(calendario.time)*/
    }
}