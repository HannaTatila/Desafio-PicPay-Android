package com.hanna.picpaydesafio.apresentacao.pagamento

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.bumptech.glide.Glide
import com.hanna.picpaydesafio.R
import com.hanna.picpaydesafio.apresentacao.cartao.CadastroCartaoActivity
import com.hanna.picpaydesafio.apresentacao.contatos.ContatosActivity
import com.hanna.picpaydesafio.util.ConstantesPersistencia
import kotlinx.android.synthetic.main.activity_pagamento.*
import kotlinx.android.synthetic.main.view_recibo.view.*
import org.jetbrains.anko.toast
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


class PagamentoActivity : AppCompatActivity() {

    //private lateinit var mContatoPreferencias: PreferenciasSeguranca
    private lateinit var mPagamentoViewModel: PagamentoViewModel
    //TODO: pegar campo
    private var mNumeroCartao: String? = ""
    private var mNumeroCartaoProtegido: String = ""
    private var mUrlImagemContato: String = ""
    private var mUsernameContato: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagamento)

        //mContatoPreferencias = PreferenciasSeguranca(this)

        capturaNumeroCartao()
        protegeNumeroCartao()
        manipulaPagamentoViewModel()
        capturaEventoAtualizacaoValor()
        controlaEventoClique()
    }

    private fun capturaNumeroCartao() {
        val pacote = intent.extras
        if (pacote != null) {
            mNumeroCartao = pacote.getString("NUMERO_CARTAO") //TODO: criar constante
        }
    }

    private fun protegeNumeroCartao() {
        mNumeroCartaoProtegido = mNumeroCartao!!.take(4)
    }

    private fun manipulaPagamentoViewModel() {
        mPagamentoViewModel = ViewModelProviders.of(this).get(PagamentoViewModel::class.java).also {
            it.buscaDadosRecebedor(this)
            recebedorObserver(it)
            transacaoObserver(it)
        }
    }

    private fun recebedorObserver(mPagamentoViewModel: PagamentoViewModel) {
        mPagamentoViewModel.dadosRecebedorLiveData.observe(this, Observer {
            it?.let { recebedor ->
                mUrlImagemContato = recebedor[ConstantesPersistencia.CHAVE_CONTATO.IMG_CONTATO].toString()
                mUsernameContato = recebedor[ConstantesPersistencia.CHAVE_CONTATO.USERNAME_CONTATO].toString()
                incorporaDadosView()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun incorporaDadosView() {
        Glide.with(this@PagamentoActivity).load(mUrlImagemContato).into(image_foto_contato)
        text_username_contato.text = mUsernameContato
        text_numero_cartao.text = getString(R.string.mastercard) + mNumeroCartaoProtegido + " •"
    }

    private fun capturaEventoAtualizacaoValor() {
        currencyedit_valor.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val valorFoiPreenchido = verificaValorPreenchido()
                if (valorFoiPreenchido) customizaTela(R.color.colorAccent) else customizaTela(R.color.corCinzaBotaoDesabilitado)
                button_pagar.isEnabled = valorFoiPreenchido
            }
        })
    }

    private fun verificaValorPreenchido(): Boolean {
        val conteudoCampoValor = currencyedit_valor.text.toString()//.toDouble()
        //TODO: transformar 0,00 em int e comparar a zero
        return conteudoCampoValor.isNotEmpty() && conteudoCampoValor != "0,00"
    }

    // Tentar melhorar o nome do metodo
    private fun customizaTela(corTexto: Int) {
        val cor = ContextCompat.getColor(this@PagamentoActivity, corTexto)
        currencyedit_valor.setTextColor(cor)
        txt_cifrao.setTextColor(cor)
    }

    private fun controlaEventoClique() {
        botao_voltar_pagamento.setOnClickListener { onBackPressed() }

        link_editar_cartao.setOnClickListener { chamaTelaCadastroCartao() }

        button_pagar.setOnClickListener {
            // criar metodo separado
            val valorPagamento = formataParaBigDecimal()
            mPagamentoViewModel.enviaDadosTransacao(this, valorPagamento)
        }
    }

    private fun formataParaBigDecimal(): BigDecimal {
        val valorFormatado = currencyedit_valor.text.toString()
            .replace(".", "", true)
            .replace(",", ".", true)
        return valorFormatado.toBigDecimal()
    }

    private fun transacaoObserver(mPagamentoViewModel: PagamentoViewModel) {
        mPagamentoViewModel.transacaoLiveData.observe(this, Observer {
            it?.let { transacao ->
                if (transacao.sucesso) {
                    //mostraCarregamento()
                    toast(getString(R.string.mag_sucesso_pagamento))
                    configuraViewRecibo(transacao.id, transacao.valor, transacao.timestamp)
                } else {
                    toast(getString(R.string.mag_recusa_pagamento))
                }
            }
        })
    }

    private fun configuraViewRecibo(idTransacao: Int, valor: BigDecimal, timestamp: String) {
        val viewRecibo = criaViewRecibo()
        incorporaDadosViewRecibbo(viewRecibo, idTransacao, valor, timestamp)
        mostraRecibo(viewRecibo)
    }

    fun criaViewRecibo(): View {
        return this.layoutInflater.inflate(R.layout.view_recibo, null)
    }

    @SuppressLint("SetTextI18n")
    private fun incorporaDadosViewRecibbo(viewRecibo: View, idTransacao: Int, valor: BigDecimal, timestamp: String) {
        Glide.with(viewRecibo.context).load(mUrlImagemContato).into(viewRecibo.img_foto_contato)
        viewRecibo.txt_username_contato.text = mUsernameContato
        viewRecibo.txt_numero_transacao.text = getString(R.string.transacao) + idTransacao.toString()
        viewRecibo.txt_dados_cartao.text = getString(R.string.cartao_master) + mNumeroCartaoProtegido

        val valorEmReal = formataBigDecimalParaMoeda(valor)
        viewRecibo.txt_valor.text = valorEmReal
        viewRecibo.txt_total_pago.text = valorEmReal

        val dataHoraAtual = buscaDataHoraAtual(timestamp)
        viewRecibo.txt_data_hora.text = dataHoraAtual
    }

    fun formataBigDecimalParaMoeda(valor: BigDecimal): String {
        val formatacaoParaReal = DecimalFormat
            .getCurrencyInstance(Locale("pt", "br"))
        return formatacaoParaReal.format(valor)
    }

    private fun mostraRecibo(viewRecibo: View) {
        val caixaDialogo = BottomSheetDialog(this)
        caixaDialogo.setContentView(viewRecibo)
        caixaDialogo.show()
    }

    private fun buscaDataHoraAtual(timestamp: String): String {
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


        val calendario = Calendar.getInstance()
        calendario.timeInMillis = timestamp.toLong()
        val formatoDataHora = SimpleDateFormat("dd-MM-yyyy 'àsss' hh:mm", Locale.getDefault())
        return formatoDataHora.format(calendario.timeInMillis)

        //val date = DateFormat.format("dd-MM-yyyy 'às' hh:mm:ss", calendario).toString()
    }

    private fun mostraCarregamento() {
        progress_bar.visibility = View.VISIBLE
        tela_pagamento.visibility = View.GONE
    }

    private fun chamaTelaCadastroCartao() {
        val intent = CadastroCartaoActivity.buscaIntent(this, mNumeroCartao)
        this.startActivity(intent)
    }

    private fun chamaTelaContatos() {
        val intent = ContatosActivity.buscaIntent(this)
        this.startActivity(intent)
    }

    companion object {
        private const val NUMERO_CARTAO = "NUMERO_CARTAO" //TODO: criar constante

        fun buscaIntent(contexto: Context, numeroCartao: String): Intent {
            val pacote = Bundle()
            pacote.putString(NUMERO_CARTAO, numeroCartao)
            return Intent(contexto, PagamentoActivity::class.java).putExtras(pacote)
        }
    }

}
